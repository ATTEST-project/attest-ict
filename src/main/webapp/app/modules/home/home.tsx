import './home.scss';

import React, { useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Button, Card, CardTitle, CardBody, CardImg, CardColumns, CardText } from 'reactstrap';

import { getLoginUrl, REDIRECT_URL } from 'app/shared/util/url-utils';

import attestHomeImg from '../../../content/images/home_static_attest_650.png';
import tasksHomeImg from '../../../content/images/home_tasks_small.png';
import toolsHomeImg from '../../../content/images/home_tools_small.png';
import networkHomeImg from '../../../content/images/home_map.png';

import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Home = () => {
  useEffect(() => {
    const redirectURL = localStorage.getItem(REDIRECT_URL);
    if (redirectURL) {
      localStorage.removeItem(REDIRECT_URL);
      location.href = `${location.origin}${redirectURL}`;
    }
  });
  const dispatch = useAppDispatch();

  return (
    <div className={'home-main-div'}>
      <div className={'home-container'}>
        <div className={'home-column1 home-image'}>
          <img src={attestHomeImg} alt="Image of the home page Ict Platform" />
        </div>
        <div className={'home-column2'}>
          <Card className={'home-card'}>
            <Link to={`/network`}>
              <CardImg alt="Image of the home page network map" src={networkHomeImg} top width="100%" />
            </Link>
            <CardBody>
              <CardTitle tag="h5">Networks</CardTitle>
            </CardBody>
          </Card>

          <Card className={'home-card'}>
            <Link to={`/tools`}>
              <CardImg alt="Image of the home page tools" src={toolsHomeImg} top width="100%" />
            </Link>
            <CardBody>
              <CardTitle tag="h5">Tools</CardTitle>
            </CardBody>
          </Card>

          <Card className={'home-card'}>
            <Link to={`/task`}>
              <CardImg alt="Image of the home page tasks" src={tasksHomeImg} top width="100%" />
            </Link>
            <CardBody>
              <CardTitle tag="h5">Tasks</CardTitle>
            </CardBody>
          </Card>
        </div>
      </div>
    </div>
  );
};

export default Home;
