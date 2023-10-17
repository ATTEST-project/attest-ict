import React from 'react';
import { Row, Col, Table } from 'reactstrap';
import Divider from 'app/shared/components/divider/divider';
import FlexCost from 'app/modules/tools/WP4/T41/tractability-tool/results/section/flex-cost';

import Charts from 'app/shared/components/T41-44/results/charts/charts';
import AllResults from 'app/modules/tools/WP4/T41/tractability-tool/results/section/all/all-results';

interface T41FlexResultsInterface {
  data: any[];
  cost: string;
  title: string;
  location: any;
}

const T41FlexResults = (props: T41FlexResultsInterface) => {
  const { data, cost, title, location } = props;

  const switchChart = () => {
    let section = 'ALL';
    switch (title) {
      case 'APC_MW':
        section = 'APC';
        break;
      case 'EES_':
      case 'FL_':
        section = title;
        break;
      default:
        section = 'ALL';
        break;
    }
    if (section === 'ALL') {
      return <AllResults location={location} />;
    } else {
      return <Charts location={location} section={section} type="scatter" />;
    }
  };

  return (
    <>
      <FlexCost cost={cost} />
      <Divider />
      {switchChart()}
    </>
  );
};

export default T41FlexResults;
