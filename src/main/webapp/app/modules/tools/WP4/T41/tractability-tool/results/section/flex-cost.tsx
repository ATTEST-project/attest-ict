import React from 'react';
import { Row, Col, Table } from 'reactstrap';
import Divider from 'app/shared/components/divider/divider';

interface FlexCostInterface {
  cost: string;
}

const FlexCost = (props: FlexCostInterface) => {
  const { cost } = props;

  return (
    <>
      <div>
        <div className="section-with-border">
          <Table>
            <thead>
              <tr>
                <th>Cost Type</th>
                <th>Value (€)</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>Flexibility Procurement</td>
                <td>{parseFloat(cost).toFixed(3)}</td>
              </tr>
            </tbody>
          </Table>
        </div>
      </div>
    </>
  );
};

export default FlexCost;
