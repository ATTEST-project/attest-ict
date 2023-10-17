import React from 'react';

import { NavItem, NavLink, NavbarBrand } from 'reactstrap';
import { NavLink as Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export const BrandIcon = props => (
  <div {...props} className="brand-icon">
    <img src="content/images/ATTEST-Logo-White.png" alt="Logo" />
  </div>
);

export const Brand = () => (
  <NavbarBrand tag={Link} to={{ pathname: 'https://attest-project.eu//' }} target="_blank" className="brand-logo">
    <BrandIcon />
    <span className="brand-title"></span>
    <span className="navbar-version">{VERSION}</span>
  </NavbarBrand>
);

export const Home = () => (
  <NavItem>
    <NavLink tag={Link} to="/" className="d-flex align-items-center">
      <FontAwesomeIcon icon="home" />
      <span>Home</span>
    </NavLink>
  </NavItem>
);

export const Network = () => (
  <NavItem>
    <NavLink tag={Link} to="/network" className="d-flex align-items-center">
      <FontAwesomeIcon icon="network-wired" />
      <span>Network</span>
    </NavLink>
  </NavItem>
);

export const Tools = () => (
  <NavItem>
    <NavLink tag={Link} to="/tools" className="d-flex align-items-center">
      <FontAwesomeIcon icon="toolbox" />
      <span>Tools</span>
    </NavLink>
  </NavItem>
);

export const Tasks = () => (
  <NavItem>
    <NavLink tag={Link} to="/task" className="d-flex align-items-center">
      <FontAwesomeIcon icon="tasks" />
      <span>Tasks</span>
    </NavLink>
  </NavItem>
);
