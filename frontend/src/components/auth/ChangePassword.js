import React, { useState } from 'react';
import authService from '../../services/auth.service';
import SimpleHeader from '../../layout/SimpleHeader';

const ChangePassword = () => {
  const [changePasswordDto, setChangePasswordDto] = useState({
    email: '', // Assuming you retrieve the email from user context or session
    currentPassword: '',
    newPassword: '',
  });

  const handleChangePassword = async (e) => {
    e.preventDefault();
    try {
      const response = await authService.changePassword(changePasswordDto);
      console.log(response);
      if (response.status === 200) 
      {
        alert(response.data.message);
        // Redirect to profile or handle accordingly
      }
       else 
       {
        alert(response.data.message);
      }
    } catch (error) {
      console.error('Error changing password:', error);
      alert('Password change failed. Please try again.');
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setChangePasswordDto({ ...changePasswordDto, [name]: value });
  };
  return (
    <>
    <SimpleHeader />
    <div className="container-fluid d-flex justify-content-center align-items-center vh-100">
      <div className="card shadow p-4 "style={{ width: '100%', maxWidth: '600px' }}>
        <div className="card-body">
          <h2 className="card-title text-center mb-4" style={{ fontFamily: 'Arial, sans-serif', fontWeight: 'bold', fontSize: '2rem', color: 'rgb(14 33 93)' }}>
            Change Password
          </h2>
          <form onSubmit={handleChangePassword}>
            <div className="form-group"style={{ fontWeight: 'bold' }}>
              <label htmlFor="email">Email: </label>
              <input
                type="email"
                className="form-control"
                name="email"
                placeholder="Email"
                value={changePasswordDto.email}
                onChange={handleChange}
                required
              />
            </div>
            <div className="form-group" style={{ fontWeight: 'bold' }}>
              <label htmlFor="password">Current Password: </label>
              <input
                type="password"
                className="form-control"
                name="currentPassword"
                placeholder="Current Password"
                value={changePasswordDto.currentPassword}
                onChange={handleChange}
                required
              />
            </div>
            <div className="form-group"style={{ fontWeight: 'bold' }}>
              <label htmlFor="password">New Password: </label>
              <input
                type="password"
                className="form-control"
                name="newPassword"
                placeholder="New Password"
                value={changePasswordDto.newPassword}
                onChange={handleChange}
                required
              />
            </div>
            <button type="submit" className="btn btn-primary btn-block mt-3" style={{ backgroundColor: 'rgb(29 21 69)', borderColor: '#28a745' }}>
              Change Password
            </button>
          </form>
        </div>
      </div>
    </div>
    </>
  );
};

export default ChangePassword;
