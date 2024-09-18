import { useEffect } from 'react';
import authService from '../../services/auth.service';
const Logout = () => {
  useEffect(() => {
    authService.logout();
    // window.location.href = '/login';
    window.location.href = '/';
  }, []);

  return null;
};

export default Logout;
