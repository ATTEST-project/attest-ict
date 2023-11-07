import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';

import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown icon="th-list" name="Entities" id="entity-menu" data-cy="entity" style={{ maxHeight: '80vh', overflow: 'auto' }}>
    <>{/* to avoid warnings when empty */}</>
    <MenuItem icon="asterisk" to="/network">
      Network
    </MenuItem>
    <MenuItem icon="asterisk" to="/base-mva">
      Base MVA
    </MenuItem>
    <MenuItem icon="asterisk" to="/bus">
      Bus
    </MenuItem>
    <MenuItem icon="asterisk" to="/bus-name">
      Bus Name
    </MenuItem>
    <MenuItem icon="asterisk" to="/bus-coordinate">
      Bus Coordinate
    </MenuItem>
    <MenuItem icon="asterisk" to="/bus-extension">
      Bus Extension
    </MenuItem>
    <MenuItem icon="asterisk" to="/branch">
      Branch
    </MenuItem>
    <MenuItem icon="asterisk" to="/load-profile">
      Load Profile
    </MenuItem>
    <MenuItem icon="asterisk" to="/load-el-val">
      Load El Val
    </MenuItem>
    <MenuItem icon="asterisk" to="/generator">
      Generator
    </MenuItem>
    <MenuItem icon="asterisk" to="/generator-extension">
      Generator Extension
    </MenuItem>
    <MenuItem icon="asterisk" to="/gen-tag">
      Gen Tag
    </MenuItem>
    <MenuItem icon="asterisk" to="/gen-profile">
      Gen Profile
    </MenuItem>
    <MenuItem icon="asterisk" to="/gen-el-val">
      Gen El Val
    </MenuItem>
    <MenuItem icon="asterisk" to="/billing-der">
      Billing Der
    </MenuItem>
    <MenuItem icon="asterisk" to="/storage">
      Storage
    </MenuItem>
    <MenuItem icon="asterisk" to="/storage-cost">
      Storage Cost
    </MenuItem>
    <MenuItem icon="asterisk" to="/tool-log-file">
      Tool Log File
    </MenuItem>
    <MenuItem icon="asterisk" to="/flex-profile">
      Flex Profile
    </MenuItem>
    <MenuItem icon="asterisk" to="/flex-el-val">
      Flex El Val
    </MenuItem>
    <MenuItem icon="asterisk" to="/flex-cost">
      Flex Cost
    </MenuItem>
    <MenuItem icon="asterisk" to="/transf-el-val">
      Transf El Val
    </MenuItem>
    <MenuItem icon="asterisk" to="/gen-cost">
      Gen Cost
    </MenuItem>
    <MenuItem icon="asterisk" to="/protection-tool">
      Protection Tool
    </MenuItem>
    <MenuItem icon="asterisk" to="/transformer">
      Transformer
    </MenuItem>
    <MenuItem icon="asterisk" to="/weather-forecast">
      Weather Forecast
    </MenuItem>
    <MenuItem icon="asterisk" to="/capacitor-bank-data">
      Capacitor Bank Data
    </MenuItem>
    <MenuItem icon="asterisk" to="/line-cable">
      Line Cable
    </MenuItem>
    <MenuItem icon="asterisk" to="/asset-transformer">
      Asset Transformer
    </MenuItem>
    <MenuItem icon="asterisk" to="/node">
      Node
    </MenuItem>
    <MenuItem icon="asterisk" to="/solar-data">
      Solar Data
    </MenuItem>
    <MenuItem icon="asterisk" to="/price">
      Price
    </MenuItem>
    <MenuItem icon="asterisk" to="/asset-ug-cable">
      Asset UG Cable
    </MenuItem>
    <MenuItem icon="asterisk" to="/topology">
      Topology
    </MenuItem>
    <MenuItem icon="asterisk" to="/output-file">
      Output File
    </MenuItem>
    <MenuItem icon="asterisk" to="/topology-bus">
      Topology Bus
    </MenuItem>
    <MenuItem icon="asterisk" to="/voltage-level">
      Voltage Level
    </MenuItem>
    <MenuItem icon="asterisk" to="/transf-profile">
      Transf Profile
    </MenuItem>
    <MenuItem icon="asterisk" to="/wind-data">
      Wind Data
    </MenuItem>
    <MenuItem icon="asterisk" to="/billing-consumption">
      Billing Consumption
    </MenuItem>
    <MenuItem icon="asterisk" to="/branch-extension">
      Branch Extension
    </MenuItem>
    <MenuItem icon="asterisk" to="/task">
      Task
    </MenuItem>
    <MenuItem icon="asterisk" to="/input-file">
      Input File
    </MenuItem>
    <MenuItem icon="asterisk" to="/dso-tso-connection">
      Dso Tso Connection
    </MenuItem>
    <MenuItem icon="asterisk" to="/tool">
      Tool
    </MenuItem>
    <MenuItem icon="asterisk" to="/branch-el-val">
      Branch El Val
    </MenuItem>
    <MenuItem icon="asterisk" to="/branch-profile">
      Branch Profile
    </MenuItem>
    <MenuItem icon="asterisk" to="/simulation">
      Simulation
    </MenuItem>
    <MenuItem icon="asterisk" to="/tool-parameter">
      Tool Parameter
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
