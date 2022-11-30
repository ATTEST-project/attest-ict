import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getLoginUrl } from 'app/shared/util/url-utils';
import { NavDropdown } from './menu-components';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getAccount } from 'app/shared/reducers/authentication';

const accountMenuItemsAuthenticated = () => (
  <>
    <MenuItem icon="info" to={'/userinfo'}>
      User Info
    </MenuItem>
    <MenuItem icon="sign-out-alt" to="/logout" data-cy="logout">
      Sign out
    </MenuItem>
  </>
);

const accountMenuItems = () => (
  <>
    <DropdownItem id="login-item" tag="a" href={getLoginUrl()} data-cy="login">
      <FontAwesomeIcon icon="sign-in-alt" /> Sign in
    </DropdownItem>
  </>
);

export const AccountMenu = ({ isAuthenticated = false }) => {
  const dispatch = useAppDispatch();

  React.useEffect(() => {
    dispatch(getAccount());
  }, []);

  const account = useAppSelector(state => state.authentication.account);

  return (
    <NavDropdown icon="user" name={account.login || 'Account'} id="account-menu" data-cy="accountMenu" style={{ right: '0%' }}>
      {isAuthenticated ? accountMenuItemsAuthenticated() : accountMenuItems()}
    </NavDropdown>
  );
};

export default AccountMenu;
