import './home_grid.scss';

import React, { useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Button, Card, CardTitle, CardBody, CardImg } from 'reactstrap';

import { getLoginUrl, REDIRECT_URL } from 'app/shared/util/url-utils';

import attest_home from '../../../content/images/home_smartenergy.png';
import task_img from '../../../content/images/home_tasks.jpg';
import tool_img from '../../../content/images/home_tools.jpg';

import EuropeMap from 'app/shared/components/map/EuropeMap';

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
      <div className={'home-container-grid'}>
        <div id="home-container-img" className={'grid-item1'}>
          <img className={'home-container-img'} alt={'attest-logo'} src={attest_home} />
        </div>

        <div id="map" className={'grid-item2'}>
          <Card className={'home-card'}>
            <Link to={`/network`}>
              <EuropeMap highlightedCountries={['ES', 'HR', 'PT', 'UK']} />
            </Link>
            <CardBody>
              <CardTitle tag="h5">Networks</CardTitle>
            </CardBody>
          </Card>
        </div>

        <div id="card-tool" className={'grid-item3'}>
          <Card className={'home-card'}>
            <Link to={`/tools`}>
              <CardImg alt="tools" src={tool_img} top width="100%" />
            </Link>
            <CardBody>
              <CardTitle tag="h5">Tools</CardTitle>
            </CardBody>
          </Card>
        </div>

        <div id="card-task" className={'grid-item4'}>
          <Card className={'home-card'}>
            <Link to={`/task`}>
              <CardImg alt="task" src={task_img} top width="100%" />
            </Link>
            <CardBody>
              <CardTitle tag="h5">Task</CardTitle>
            </CardBody>
          </Card>
        </div>
      </div>
    </div>
  );
};

export default Home;
