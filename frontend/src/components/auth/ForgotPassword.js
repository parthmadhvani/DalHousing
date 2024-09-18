import React, { useState } from 'react';
import authService from '../../services/auth.service';

const ForgotPassword = () => {
  const [email, setEmail] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [message, setMessage] = useState('');

  const handleResetRequest = async (e) => {
    e.preventDefault();
    setError('');
    setMessage('');
    setLoading(true);

    try {
      const response = await authService.requestPasswordReset(email);
      if (response.status === 200) 
      {
        alert("Email sent sucessfully");
        setMessage(response.data.message);
      } else {
        setError('Failed to send reset link. Please try again.');
      }
    } catch (error) {
      setError('Failed to send reset link. Please try again later.');
      console.error('Reset request error:', error);
    } finally {
      setLoading(false);
    }
  };
  return (
    <div className="container-fluid d-flex justify-content-center align-items-center vh-100">
      <div className="card shadow p-4" style={{ width: '100%', maxWidth: '600px' }}>
        <div className="card-body">
          <h2 className="card-title text-center mb-4" style={{ fontFamily: 'Arial, sans-serif', fontWeight: 'bold', fontSize: '2rem', color: '#6f42c1' }}>
            Forgot Password
          </h2>
          <form onSubmit={handleResetRequest}>
            <div className="form-group" style={{ fontWeight: 'bold' }}>
            <label htmlFor="email">Email:</label>
              <input
                type="email"
                className="form-control"
                placeholder="Enter your email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
              />
            </div>
            {message && <div className="alert alert-success">{message}</div>}
            {error && <div className="alert alert-danger">{error}</div>}
            <button type="submit" className="btn btn-primary btn-block mt-3" style={{ backgroundColor: '#28a745', borderColor: '#28a745' }} disabled={loading}>
              {loading ? 'Sending...' : 'Send Reset Link'}
            </button>
          </form>
        </div>
      </div>
    </div>
  );
};

export default ForgotPassword;
