import { Button, Card, CardText, CardTitle } from 'reactstrap';
import { Link } from 'react-router-dom';
import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import CustomTooltip from 'app/shared/components/tooltip/custom-tooltip';
import { useAppSelector } from 'app/config/store';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { AUTHORITIES } from 'app/config/constants';

const ToolCard = props => {
  const { index, title, text, toPage, isReady, type, network } = props;

  const authentication = useAppSelector(state => state.authentication.account);
  const isDSO = hasAnyAuthority(authentication.authorities, [AUTHORITIES.DSO]);
  const isTSO = hasAnyAuthority(authentication.authorities, [AUTHORITIES.TSO]);
  const isAdmin = hasAnyAuthority(authentication.authorities, [AUTHORITIES.ADMIN]);

  const showForDSO = (type === 'all' || type === 'distribution') && isDSO;
  const showForTSO = (type === 'all' || type === 'transmission') && isTSO;

  return isAdmin || showForDSO || showForTSO ? (
    <Card color="light" outline body>
      <CardTitle tag="h5">{title}</CardTitle>
      <CardText>{text}</CardText>
      <div id={'conf-run-button-' + index} style={{ marginTop: 'auto' }}>
        <Button
          disabled={!network || !isReady}
          color={isReady ? 'primary' : 'secondary'}
          tag={Link}
          to={{ pathname: toPage, network }}
          style={{ width: '100%' }}
        >
          {isReady ? (
            <>
              <FontAwesomeIcon icon="cog" />
              {' Configure and run'}
            </>
          ) : (
            'Tool not ready'
          )}
        </Button>
      </div>
      {!network && isReady && <CustomTooltip target={'conf-run-button-' + index} tooltip="First select a network" />}
      {network && isReady && <CustomTooltip target={'conf-run-button-' + index} tooltip={'Last network selected: ' + network.mpcName} />}
    </Card>
  ) : null;
};

export default ToolCard;
