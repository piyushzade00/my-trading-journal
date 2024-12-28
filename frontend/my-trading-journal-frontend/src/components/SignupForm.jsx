import React from 'react';

const SignupForm = () => {
  return (
    <section className="container forms">
      <div className="signup-header">
        <h1>Get Started</h1>
        <span className="content">
          Already have an account?{' '}
          <a href="/login" className="link login-link">
            Login to existing account
          </a>
        </span>
      </div>

      <div className="form signup">
        <div className="form-content">
          <header>Sign Up</header>
          <form>
            <div className="field input-field">
              <input type="text" placeholder="Your Name" className="input" />
            </div>

            <div className="field input-field">
              <input type="email" placeholder="Your Email Address" className="input" />
            </div>

            <div className="field input-field">
              <input type="password" placeholder="Password" className="password" />
            </div>

            <div className="field input-field">
              <input type="password" placeholder="Confirm Password" className="password" />
            </div>

            <div className="field button-field">
              <button type="submit">Sign Up</button>
            </div>
          </form>
        </div>

        <div className="line"></div>

        <div className="media-options">
            <a href="#" className="field google">
            <img src="/src/assets/google_icon.png" alt="" className="google-img signup"></img>
            <span>Continue with Google</span>
            </a>
        </div>

      </div>
    </section>
  );
};

export default SignupForm;
