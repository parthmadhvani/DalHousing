// Signup.js
/*
import React, { useState } from 'react';
import authService from '../../services/auth.service';

const Signup = () => {
  const [registerUserDto, setRegisterUserDto] = useState({
    email: '',
    password: '',
    userName: '',
    firstName: '',
    lastName: '',
    mobileNumber: '',
    role: '', // 'lister' or 'seeker'
  });
  const [message, setMessage] = useState(null); // State for alert messages
  const [messageType, setMessageType] = useState(''); // State for alert type
  const handleSignup = async (e) => {
    e.preventDefault();
    try {
      const response = await authService.register({...registerUserDto});
      console.log('Signup response:', response); // Debugging line
      if (response && response.status === 200) {
        setMessageType('success');
        setMessage('Signup successful!');
        // Redirect to login page or dashboard after a short delay
        setTimeout(() => {
          window.location.href = '/login';
        }, 2000);
      } else {
        setMessageType('danger');
        setMessage('Signup failed. Please try again.');
      }
    } catch (error) {
      console.error('Error signing up:', error);
      setMessageType('danger');
      setMessage('Signup failed. Please try again.');
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setRegisterUserDto({ ...registerUserDto, [name]: value });
  };
  return (
    <div className="container-fluid d-flex justify-content-center align-items-center min-vh-100">
      <div className="card shadow p-4" style={{ width: '100%', maxWidth: '600px', borderRadius: '10px' , maxHeight: '80vh', overflow: 'auto'}}>
        <div className="card-body">
          <h2 className="card-title text-center mb-4" style={{ fontFamily: 'Arial, sans-serif', fontWeight: 'bold', fontSize: '2rem', color: '#6f42c1' }}>Register</h2>
          <form onSubmit={handleSignup}>
            <div className="form-group mb-3">
              <label htmlFor="email">Email:</label>
              <input
                type="email"
                className="form-control"
                name="email"
                placeholder="Email"
                value={registerUserDto.email}
                onChange={handleChange}
                required
              />
            </div>
            <div className="form-group mb-3">
              <label htmlFor="password">Password:</label>
              <input
                type="password"
                className="form-control"
                name="password"
                placeholder="Password"
                value={registerUserDto.password}
                onChange={handleChange}
                required
              />
            </div>
            <div className="form-group mb-3">
              <label htmlFor="userName">User Name:</label>
              <input
                type="text"
                className="form-control"
                name="userName"
                placeholder="Username"
                value={registerUserDto.userName}
                onChange={handleChange}
                required
              />
            </div>
            <div className="form-group mb-3">
              <label htmlFor="firstName">First Name:</label>
              <input
                type="text"
                className="form-control"
                name="firstName"
                placeholder="First Name"
                value={registerUserDto.firstName}
                onChange={handleChange}
                required
              />
            </div>
            <div className="form-group mb-3">
              <label htmlFor="lastName">Last Name:</label>
              <input
                type="text"
                className="form-control"
                name="lastName"
                placeholder="Last Name"
                value={registerUserDto.lastName}
                onChange={handleChange}
                required
              />
            </div>
            <div className="form-group mb-3">
              <label htmlFor="mobileNumber">Mobile Number:</label>
              <input
                type="text"
                className="form-control"
                name="mobileNumber"
                placeholder="Mobile Number"
                value={registerUserDto.mobileNumber}
                onChange={handleChange}
                required
              />
            </div>
            <div className="form-group mb-3">
              <label htmlFor="role">Role:</label>
              <select
                className="form-control"
                name="role"
                value={registerUserDto.role}
                onChange={handleChange}
                required
              >
                <option value="">Select Role</option>
                <option value="LISTER">Lister</option>
                <option value="SEEKER">Seeker</option>
              </select>
            </div>
            <button type="submit" className="btn btn-primary btn-block mt-3" style={{ backgroundColor: '#28a745', borderColor: '#28a745' }}>SignUp</button>
          </form>
          {message && (
            <div className={`alert alert-${messageType} mt-3`} role="alert">
              {message}
            </div>
          )}
        </div>
      </div>
    </div>
  );
  
  
};

export default Signup;
*/
// Signup.js

import React, { useState } from 'react';
import authService from '../../services/auth.service';

const Signup = () => {
  const [registerUserDto, setRegisterUserDto] = useState({
    email: '',
    password: '',
    userName: '',
    firstName: '',
    lastName: '',
    mobileNumber: '',
    role: '', // 'lister' or 'seeker'
  });
  const [message, setMessage] = useState(null); // State for alert messages
  const [messageType, setMessageType] = useState(''); // State for alert type
  const handleSignup = async (e) => {
    e.preventDefault();
    try {
      const response = await authService.register({ ...registerUserDto });
      console.log('Signup response:', response); // Debugging line
      if (response && response.status === 200) {
        setMessageType('success');
        setMessage('Signup successful!');
        // Redirect to login page or dashboard after a short delay
        setTimeout(() => {
          window.location.href = '/login';
        }, 2000);
      } else {
        setMessageType('danger');
        setMessage('Signup failed. Please try again.');
      }
    } catch (error) {
      console.error('Error signing up:', error);
      setMessageType('danger');
      setMessage('Signup failed. Please try again.');
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setRegisterUserDto({ ...registerUserDto, [name]: value });
  };

  return (
    <div className="container-fluid d-flex justify-content-center align-items-center min-vh-100">
      <div className="card shadow p-4" style={{ width: '100%', maxWidth: '600px', borderRadius: '10px', maxHeight: '80vh', overflow: 'hidden' }}>
        <div className="card-header text-center" style={{ backgroundColor: 'white', borderBottom: 'none' }}>
          <h2 className="card-title" style={{ fontFamily: 'Arial, sans-serif', fontWeight: 'bold', fontSize: '2rem', color: 'rgb(14, 33, 93)' }}>Register</h2>
        </div>
        <div className="card-body overflow-auto" style={{ maxHeight: 'calc(80vh - 60px)', paddingTop: '0' }}>
          <form onSubmit={handleSignup}>
            <div className="form-group mb-3"style={{ fontWeight: 'bold' }}>
              <label htmlFor="email">Email:</label>
              <input
                type="email"
                className="form-control"
                name="email"
                placeholder="Email"
                value={registerUserDto.email}
                onChange={handleChange}
                required
              />
            </div>
            <div className="form-group mb-3"style={{ fontWeight: 'bold' }}>
              <label htmlFor="password">Password:</label>
              <input
                type="password"
                className="form-control"
                name="password"
                placeholder="Password"
                value={registerUserDto.password}
                onChange={handleChange}
                required
              />
            </div>
            <div className="form-group mb-3"style={{ fontWeight: 'bold' }}>
              <label htmlFor="userName">User Name:</label>
              <input
                type="text"
                className="form-control"
                name="userName"
                placeholder="Username"
                value={registerUserDto.userName}
                onChange={handleChange}
                required
              />
            </div>
            <div className="form-group mb-3"style={{ fontWeight: 'bold' }}>
              <label htmlFor="firstName">First Name:</label>
              <input
                type="text"
                className="form-control"
                name="firstName"
                placeholder="First Name"
                value={registerUserDto.firstName}
                onChange={handleChange}
                required
              />
            </div>
            <div className="form-group mb-3"style={{ fontWeight: 'bold' }}>
              <label htmlFor="lastName">Last Name:</label>
              <input
                type="text"
                className="form-control"
                name="lastName"
                placeholder="Last Name"
                value={registerUserDto.lastName}
                onChange={handleChange}
                required
              />
            </div>
            <div className="form-group mb-3"style={{ fontWeight: 'bold' }}>
              <label htmlFor="mobileNumber">Mobile Number:</label>
              <input
                type="text"
                className="form-control"
                name="mobileNumber"
                placeholder="Mobile Number"
                value={registerUserDto.mobileNumber}
                onChange={handleChange}
                required
              />
            </div>
            <div className="form-group mb-3"style={{ fontWeight: 'bold' }}>
              <label htmlFor="role">Role:</label>
              <select
                className="form-control"
                name="role"
                value={registerUserDto.role}
                onChange={handleChange}
                required
              >
                <option value="">Select Role</option>
                <option value="LISTER">Lister</option>
                <option value="SEEKER">Seeker</option>
              </select>
            </div>
            <button type="submit" className="btn btn-primary btn-block mt-3" style={{ backgroundColor: 'rgb(29 21 69)', borderColor: 'rgb(14, 33, 93)' }}>SignUp</button>
          </form>
          {message && (
            <div className={`alert alert-${messageType} mt-3`} role="alert">
              {message}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Signup;

