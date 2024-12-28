import React, { useState } from 'react';
import axios from 'axios';

const LoginForm = () => {
  // State to manage login inputs
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleLogin = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const response = await axios.post('http://localhost:8080/users/authenticate_login', {
        emailAddress: email,
        password,
      });

      const { token } = response.data;
      console.log("Token - " + token);

      localStorage.setItem('authToken', token);

      window.location.href = '/dashboard';
    } catch (err) {
     setError(err.response?.data?.message || 'Login failed. Incorrect Credentials.');
    } finally {
      setLoading(false);
    }
  };

  const togglePasswordVisibility = () => {
    setShowPassword((prev) => !prev);
  };

  return (
    <section className="container forms">
        <div className="signup-header">
                    <h1>Hi, Welcome Back!</h1>
                    <p className="content">Enter your credentials to access your account.</p>
        </div>

      <div className="form login">
        <div className="form-content">
          <header>Login</header>
          <form onSubmit={handleLogin}>
            <div className="field input-field">
              <input
                type="email"
                placeholder="Your Email Address"
                className="input"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
              />
            </div>

            <div className="field input-field">
              <input
                type={showPassword ? 'text' : 'password'}
                placeholder="Password"
                className="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
              <i
                className={`bx ${showPassword ? 'bx-show' : 'bx-hide'} eye-icon`}
                onClick={togglePasswordVisibility}
              ></i>
            </div>

            <div className="form-link">
              <a href="/forgot-password" className="forgot-pass">
                Forgot Password?
              </a>
            </div>

            {error && <p className="error-message">{error}</p>}

            <div className="field button-field">
              <button type="submit" disabled={loading}>
                {loading ? 'Logging in...' : 'Login'}
              </button>
            </div>
          </form>

          <div className="form-link">
            <span>
              Don't have an account?{' '}
              <a href="/signup" className="link signup-link">
                Signup
              </a>
            </span>
          </div>
        </div>
        
        <div className="line"></div>

        <div className="media-options">
            <a href="#" className="field google">
                <img src="/src/assets/google_icon.png" alt="" className="google-img"></img>
                <span>Continue with Google</span>
             </a>
        </div>
      </div>
    </section>
  );
};

export default LoginForm;
