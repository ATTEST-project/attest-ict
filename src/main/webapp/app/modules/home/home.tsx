import './home.scss';

import React, { useEffect } from 'react';
import { Link } from 'react-router-dom';

import attest_logo from '../../../content/images/ATTEST-Logo.png';

import { getLoginUrl, REDIRECT_URL } from 'app/shared/util/url-utils';
import { useAppSelector } from 'app/config/store';

export const Home = () => {
  useEffect(() => {
    const redirectURL = localStorage.getItem(REDIRECT_URL);
    if (redirectURL) {
      localStorage.removeItem(REDIRECT_URL);
      location.href = `${location.origin}${redirectURL}`;
    }
  });

  return (
    <div className={'home-main-div'}>
      <h2>Welcome to ATTEST!</h2>
      <div className={'home-container'}>
        <img className={'home-logo'} alt={'attest-logo'} src={attest_logo} />
      </div>
    </div>
  );
};

export default Home;
