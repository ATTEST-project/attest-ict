import React from 'react';
import case3_outputs from '../../sample-data/case3_outputs.json';
import { Table } from 'reactstrap';
import Divider from 'app/shared/components/divider/divider';
import { useAppSelector } from 'app/config/store';

const TotalInvestCostYear = () => {
  const jsonResponse = useAppSelector(state => state.t31ToolResults.entity);

  return (
    <div className="section-with-border">
      <h6>Total investment cost (EUR-million/year)</h6>
      <Table>
        <thead>
          <tr>
            <th>Scenario</th>
            <th>2020</th>
            <th>2030</th>
            <th>2040</th>
            <th>2050</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>Scenario 1 (Active economy)</td>
            <td>{jsonResponse['Scenario 1 (Active economy)']['2020']['Total investment cost (EUR-million)']}</td>
            <td>{jsonResponse['Scenario 1 (Active economy)']['2030']['Total investment cost (EUR-million)']}</td>
            <td>{jsonResponse['Scenario 1 (Active economy)']['2040']['Total investment cost (EUR-million)']}</td>
            <td>{jsonResponse['Scenario 1 (Active economy)']['2050']['Total investment cost (EUR-million)']}</td>
          </tr>
          <tr>
            <td>Scenario 2 (Slow economy)</td>
            <td>{jsonResponse['Scenario 2 (Slow economy)']['2020']['Total investment cost (EUR-million)']}</td>
            <td>{jsonResponse['Scenario 2 (Slow economy)']['2030']['Total investment cost (EUR-million)']}</td>
            <td>{jsonResponse['Scenario 2 (Slow economy)']['2040']['Total investment cost (EUR-million)']}</td>
            <td>{jsonResponse['Scenario 2 (Slow economy)']['2050']['Total investment cost (EUR-million)']}</td>
          </tr>
        </tbody>
      </Table>
    </div>
  );
};

export default TotalInvestCostYear;
