import React from 'react';
import { Switch } from 'react-router-dom';
import Loadable from 'react-loadable';

import LoginRedirect from 'app/modules/login/login-redirect';
import Logout from 'app/modules/login/logout';
import Home from 'app/modules/home/home';
import Entities from 'app/entities';
import PrivateRoute from 'app/shared/auth/private-route';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';
import PageNotFound from 'app/shared/error/page-not-found';
import { AUTHORITIES } from 'app/config/constants';
import Tools from 'app/modules/tools';
import WorkInProgress from 'app/modules/wip/wip';
import UserInfo from 'app/modules/userinfo/userinfo';
import Task from 'app/entities/task/task';

const Admin = Loadable({
  loader: () => import(/* webpackChunkName: "administration" */ 'app/modules/administration'),
  loading: () => <div>loading ...</div>,
});

const Routes = () => {
  return (
    <div className="view-routes">
      <Switch>
        <ErrorBoundaryRoute path="/logout" component={Logout} />
        <PrivateRoute path="/admin" component={Admin} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
        <ErrorBoundaryRoute path="/" exact component={Home} />
        <PrivateRoute path="/tools" component={Tools} hasAnyAuthorities={[AUTHORITIES.USER]} />
        <ErrorBoundaryRoute exact path="/userinfo" component={UserInfo} />
        <PrivateRoute path="/tasks" component={Task} hasAnyAuthorities={[AUTHORITIES.USER]} />
        <ErrorBoundaryRoute path="/wip" exact component={WorkInProgress} />
        <ErrorBoundaryRoute path="/oauth2/authorization/oidc" component={LoginRedirect} />
        <PrivateRoute path="/" component={Entities} hasAnyAuthorities={[AUTHORITIES.USER]} />
        <ErrorBoundaryRoute component={PageNotFound} />
      </Switch>
    </div>
  );
};

export default Routes;
