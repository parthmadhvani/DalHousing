// auth.service.js

import axios from 'axios';

const API_URL = 'http://172.17.0.139:8080/api/auth/';

class AuthService {
  login(email, password) {
    return axios
      .post(API_URL + 'login', { email, password })
      .then(response => {
        if (response.data.token) {
          localStorage.setItem('user', JSON.stringify(response.data));
          localStorage.setItem('token', response.data.token);
        }
        return response.data;
      });
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    localStorage.removeItem('userId');
  }

  register({ email, password, userName, firstName, lastName, mobileNumber, role }) {
    return axios.post(API_URL + 'signup', {
      email,
      password,
      userName,
      firstName,
      lastName,
      mobileNumber,
      role
    });
  }

  changePassword({ email, currentPassword, newPassword }) {
    return axios.post(API_URL + 'change-password', {
      email,
      currentPassword,
      newPassword
    }).then(response => response);
  }
  getCurrentUser() {
    return JSON.parse(localStorage.getItem('user'));
  }

  requestPasswordReset(email) {
    return axios.post(API_URL + 'forgot-password', { email })
      .then(response => response)
      .catch(error => {
        throw new Error(error.response.data.message || 'Failed to send reset link');
      });
  }

  resetPassword({ resetToken, newPassword }) {
    return axios.post(API_URL + 'reset-password', {
      resetToken,
      newPassword
    }).then(response => response)
      .catch(error => {
        throw new Error(error.response.data.message || 'Failed to reset password');
      });
  }
  getUserIdByEmail(email) {
    return axios.get(`${API_URL}user-id?email=${email}`)
      .then(response => response.data)
      .catch(error => {
        throw new Error(error.response.data.message || 'Failed to fetch user ID');
      });
  }
  getRoleByEmail(email) {
    return axios
      .get(`${API_URL}role-id?email=${email}`)
      .then(response => response.data.id)
      .catch(error => {
        throw new Error(error.response.data.message || 'Failed to fetch user role');
      });
  }
}
const authService = new AuthService();
export default authService;
