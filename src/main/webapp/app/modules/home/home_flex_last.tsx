import './home.scss';

import React, { useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Button, Card, CardText, CardTitle, CardBody, CardColumns, CardSubtitle, CardImg } from 'reactstrap';

import { getLoginUrl, REDIRECT_URL } from 'app/shared/util/url-utils';

import attest_home from '../../../content/images/home_smartenergy.png';
import task_img from '../../../content/images/task.png';

import EuropeMap from 'app/shared/components/map/EuropeMap';
import Carousel from 'react-elastic-carousel';
import Divider from 'app/shared/components/divider/divider';
import { storeWP } from 'app/shared/reducers/tool-config';
import toolsInfo from 'app/modules/tools/info/tools-info';

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

  const handleItemClicked = (wp: string) => {
    /* eslint-disable-next-line no-console */
    console.log('handleItemClicked wp:', wp);
    const wpInfo = toolsInfo[wp];
    /* eslint-disable-next-line no-console */
    console.log('handleItemClicked:', wpInfo);
    dispatch(storeWP(wp));
  };

  return (
    <div className={'home-main-div'}>
      <div className={'home-container'}>
        <div id="first-column" className={'first-column'}>
          <img className={'home-container-img'} alt={'attest-logo'} src={attest_home} />
        </div>

        <div id="second-column" className={'second-column'}>
          <div id="network">
            <Link to={`/network`}>
              <EuropeMap highlightedCountries={['ES', 'HR', 'PT', 'UK']} />
            </Link>
          </div>

          <Card className={'home-card'}>
            <Link to={`/tools`}>
              <CardImg alt="tools" src={toolsInfo[0].src} top width="100%" />
            </Link>
            <CardBody>
              <CardTitle tag="h5">Tools</CardTitle>
            </CardBody>
          </Card>

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
