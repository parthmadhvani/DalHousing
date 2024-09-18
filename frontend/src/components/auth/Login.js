import React, { useState } from 'react';
import authService from '../../services/auth.service';
import { Link, useNavigate } from 'react-router-dom';


const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const navigate = useNavigate();
 

  const handleLogin = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const response = await authService.login(email, password);
      console.log(response);
      if (response.token) 
      {
        // Store token in localStorage
        localStorage.setItem('token', response.token);
        // Fetch user ID after successful login
        const userId = await authService.getUserIdByEmail(email);
        localStorage.setItem('userId', userId);
        // Fetch user role using email
        const role = await authService.getRoleByEmail(email);
        localStorage.setItem('roleId',role);

        console.log(role);
        // Redirect based on user role with userId in the query parameter
        if (role === 3) {
          // navigate(`/dashboard/${userId}`);
          navigate(`/dashboard`, {state:{userId:userId}});
        } 
        else if (role === 2) 
        {
          navigate('/search-filter');
        } 
        else if (role === 1) 
        {
          navigate('/Admindashboard');
        } 
    }
      else 
      {
        setError('Invalid userName or password.');
      }
    } catch (error) {
      setError('Failed to login. Please try again later.');
      console.error('Login error:', error);
    } finally {
      setLoading(false);
    }
  };
  /*
  return (
    <div className="container-fluid d-flex justify-content-center align-items-center vh-100 ">
      <div className="card shadow p-4 login-card">
        <div className="card-body">
          <h2 className="card-title text-center mb-4" style={{ fontFamily: 'Arial, sans-serif', fontWeight: 'bold', fontSize: '2rem', color: 'rgb(14 33 93)' }} >Login</h2>
          <form onSubmit={handleLogin}>
            <div className="form-group">
              <label htmlFor="email">Email:</label>
              <input
                type="email"
                className="form-control"
                placeholder="Email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
              />
            </div>
            <div className="form-group">
              <label htmlFor="password">Password:</label>
              <input
                type="password"
                className="form-control"
                placeholder="Password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
            </div>
            {error && <div className="alert alert-danger mt-3">{error}</div>}
            <button type="submit" className="btn btn-primary btn-block mt-3" style={{ backgroundColor: 'rgb(29 21 69)', borderColor: '#28a745' }} disabled={loading}>
              {loading ? 'Logging in...' : 'Login'}
            </button>
            <div className="mt-3 text-center">
              <Link to="/forgot-password">Forgot Password?</Link>
            </div>
            <p className="mt-3 text-center" style={{ color: 'black' }}>
              Don't have an account? <Link to="/signup" style={{fontWeight: 'normal' }}>Sign up here</Link>
            </p>
          </form>
        </div>
      </div>
    </div>
  );
  */
  return (
    <div className="container-fluid d-flex justify-content-center align-items-center vh-100">
      <div className="card shadow p-4 login-card">
        <div className="card-body">
          <h2 className="card-title text-center mb-4" style={{ fontFamily: 'Arial, sans-serif', fontWeight: 'bold', fontSize: '2rem', color: 'rgb(14, 33, 93)' }}>Login</h2>
          <form onSubmit={handleLogin}>
            <div className="form-group" style={{ fontWeight: 'bold' }}>
              <label htmlFor="email">Email:</label>
              <input
                type="email"
                className="form-control"
                placeholder="Email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
              />
            </div>
            <div className="form-group" style={{ fontWeight: 'bold' }}>
              <label htmlFor="password">Password:</label>
              <input
                type="password"
                className="form-control"
                placeholder="Password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
            </div>
            {error && <div className="alert alert-danger mt-3">{error}</div>}
            <button
              type="submit"
              className="btn btn-primary btn-block mt-3"
              style={{
                backgroundColor: 'rgb(14, 33, 93)',
                borderColor: '#28a745',
                fontWeight: 'bold',
                display: 'block',
                margin: '0 auto'
              }}
              disabled={loading}
            >
              {loading ? 'Logging in...' : 'Login'}
            </button>
            <div className="mt-3 text-center">
              <Link to="/forgot-password" style={{ fontWeight: 'bold' }}>Forgot Password?</Link>
            </div>
            <p className="mt-3 text-center" style={{ color: 'black',fontWeight: 'bold' }}>
              Don't have an account? <Link to="/signup" style={{ fontWeight: 'bold' }}>Sign up here</Link>
            </p>
          </form>
        </div>
      </div>
    </div>
  );
  
  
};

export default Login;
