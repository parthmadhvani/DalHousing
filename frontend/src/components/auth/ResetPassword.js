// ResetPassword.js
/*
import React, { useEffect, useState } from 'react';
import authService from '../../services/auth.service';
import { useSearchParams } from 'react-router-dom';

const ResetPassword = () => {
  const [queryParameters] = useSearchParams();
  const [resetPasswordDto, setResetPasswordDto] = useState({
    resetToken: '',
    newPassword: '',
  });

  useEffect(() => {
    setResetPasswordDto((data) => {return {...data, resetToken: queryParameters.get('token')}});
  }, [queryParameters])
  

  const handleResetPassword = async (e) => {
    e.preventDefault();
    try {
      const response = await authService.resetPassword(resetPasswordDto);
      if (response.status === 200) {
        alert('Password reset successful!');
      } else {
        alert('Password reset failed. Please try again.');
      }
    } catch (error) {
      console.error('Error resetting password:', error);
      alert('Password reset failed. Please try again.');
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setResetPasswordDto({ ...resetPasswordDto, [name]: value });
  };
  return (
    <div className="container-fluid d-flex justify-content-center align-items-center vh-100">
      <div className="card shadow p-4 login-card" style={{ width: '100%',maxWidth: '600px' }}>
        <div className="card-body">
          <h2 className="card-title text-center mb-4" style={{ fontFamily: 'Arial, sans-serif', fontWeight: 'bold', fontSize: '2rem', color: '#6f42c1' }}>
            Reset Password
          </h2>
          <form onSubmit={handleResetPassword}>
            <div className="form-group">
              <input
                type="text"
                className="form-control"
                name="resetToken"
                placeholder="Reset Token"
                value={resetPasswordDto.resetToken}
                onChange={handleChange}
                required
              />
            </div>
            <div className="form-group">
              <input
                type="password"
                className="form-control"
                name="newPassword"
                placeholder="New Password"
                value={resetPasswordDto.newPassword}
                onChange={handleChange}
                required
              />
            </div>
            <button type="submit" className="btn btn-primary btn-block mt-3" style={{ backgroundColor: '#28a745', borderColor: '#28a745' }}>
              Reset Password
            </button>
          </form>
        </div>
      </div>
    </div>
  );
};

export default ResetPassword;
*/
import React, { useEffect, useState } from 'react';
import authService from '../../services/auth.service';
import { useSearchParams } from 'react-router-dom';

const ResetPassword = () => {
  const [queryParameters] = useSearchParams();
  const [resetPasswordDto, setResetPasswordDto] = useState({
    resetToken: '',
    newPassword: '',
  });
  const [errorMessage, setErrorMessage] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    const token = queryParameters.get('token');
    if (token) {
      setResetPasswordDto((prevData) => ({ ...prevData, resetToken: token }));
    }
  }, [queryParameters]);

  const handleResetPassword = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    setErrorMessage('');
    try {
      const response = await authService.resetPassword(resetPasswordDto);
      if (response.status === 200) {
        alert('Password reset successful!');
      } else {
        setErrorMessage('Password reset failed. Please try again.');
      }
    } catch (error) {
      console.error('Error resetting password:', error);
      setErrorMessage('Password reset failed. Please try again.');
    } finally {
      setIsLoading(false);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setResetPasswordDto((prevData) => ({ ...prevData, [name]: value }));
  };

  return (
    <div className="container-fluid d-flex justify-content-center align-items-center vh-100">
      <div className="card shadow p-4 login-card" style={{ width: '100%', maxWidth: '600px' }}>
        <div className="card-body">
          <h2 className="card-title text-center mb-4" style={{ fontFamily: 'Arial, sans-serif', fontWeight: 'bold', fontSize: '2rem', color: '#6f42c1' }}>
            Reset Password
          </h2>
          {errorMessage && <div className="alert alert-danger">{errorMessage}</div>}
          <form onSubmit={handleResetPassword}>
            <div className="form-group" style={{ fontWeight: 'bold' }}>
            <label htmlFor="password">New Password:</label>
              <input
                type="password"
                className="form-control"
                name="newPassword"
                placeholder="New Password"
                value={resetPasswordDto.newPassword}
                onChange={handleChange}
                required
              />
            </div>
            <button type="submit" className="btn btn-primary btn-block mt-3" style={{ backgroundColor: '#28a745', borderColor: '#28a745' }} disabled={isLoading}>
              {isLoading ? 'Resetting...' : 'Reset Password'}
            </button>
          </form>
        </div>
      </div>
    </div>
  );
};

export default ResetPassword;
