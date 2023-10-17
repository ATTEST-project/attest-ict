import React from 'react';
import { Row, Col, Table } from 'reactstrap';
import Divider from 'app/shared/components/divider/divider';

import Charts from 'app/shared/components/T41-44/results/charts/charts';
import AllResults from 'app/modules/tools/WP4/T45/results/section/all/all-results';

interface T45FlexResultsInterface {
  data: any[];
  title: string;
  location: any;
  section: string;
}

const T45FlexResults = (props: T45FlexResultsInterface) => {
  const { data, title, location, section } = props;

  const switchChart = () => {
    if (section === 'ALL') {
      return <AllResults location={location} />;
    } else {
      return <Charts location={location} section={section} type="bar" />;
    }
  };

  return (
    <>
      <Divider />
      {switchChart()}
    </>
  );
};

export default T45FlexResults;
