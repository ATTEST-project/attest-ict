import { Button, Card, CardText, CardTitle } from 'reactstrap';
import { Link } from 'react-router-dom';
import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import CustomTooltip from 'app/shared/components/tooltip/custom-tooltip';
import { useAppSelector } from 'app/config/store';

import { shouldShowToolCard, shouldShowButtonConfiguredAndRun } from 'app/shared/util/authorizationUtils';

const ToolCard = props => {
  const { index, title, text, toPage, isActive, supportedNetworkType, network } = props;
  const currentUser = useAppSelector(state => state.authentication.account);
  const authorities = currentUser.authorities;

  const isConfAndRunEnabled = shouldShowButtonConfiguredAndRun(authorities, supportedNetworkType, isActive, network);

  return shouldShowToolCard(authorities, supportedNetworkType) ? (
    <Card color="light" outline body>
      <CardTitle tag="h5">{title}</CardTitle>
      <CardText>{text}</CardText>
      <div id={'conf-run-button-' + index} style={{ marginTop: 'auto' }}>
        <Button
          disabled={!isConfAndRunEnabled}
          color={isActive ? 'primary' : 'secondary'}
          tag={Link}
          to={{ pathname: toPage, network }}
          style={{ width: '100%' }}
        >
          {isActive ? (
            <>
              <FontAwesomeIcon icon="cog" />
              {' Configure and run'}
            </>
          ) : (
            'Tool not active'
          )}
        </Button>
      </div>
      {!network && isActive && <CustomTooltip target={'conf-run-button-' + index} tooltip="First select a network" />}
      {network && isActive && <CustomTooltip target={'conf-run-button-' + index} tooltip={'Last network selected: ' + network.mpcName} />}
    </Card>
  ) : null;
};

export default ToolCard;
