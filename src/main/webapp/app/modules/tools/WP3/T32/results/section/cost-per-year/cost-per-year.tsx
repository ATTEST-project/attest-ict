import React from 'react';
import { Table } from 'reactstrap';
import { useAppSelector } from 'app/config/store';

const CostPerYear = () => {
  const jsonResponse = useAppSelector(state => state.t32ToolResults.entity);
  /* eslint-disable-next-line no-console */
  console.log('T32 Cost per Year: start');
  return (
    <div className="section-with-border">
      <h6>Operation cost (EUR-million/year)</h6>
      <Table>
        <thead>
          <tr>
            <th>Scenario</th>
            <th>2020</th>
            <th>2030</th>
            <th>2040</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>Scenario 1</td>
            <td>{jsonResponse['Scenario 1']['2020'] ? jsonResponse['Scenario 1']['2020']['Operation cost (EUR-million/year)'] : ''}</td>
            <td>{jsonResponse['Scenario 1']['2030'] ? jsonResponse['Scenario 1']['2030']['Operation cost (EUR-million/year)'] : ''}</td>
            <td>{jsonResponse['Scenario 1']['2040'] ? jsonResponse['Scenario 1']['2040']['Operation cost (EUR-million/year)'] : ''}</td>
          </tr>
          <tr>
            <td>Scenario 2</td>
            <td>{jsonResponse['Scenario 2']['2020'] ? jsonResponse['Scenario 2']['2020']['Operation cost (EUR-million/year)'] : ''}</td>
            <td>{jsonResponse['Scenario 2']['2030'] ? jsonResponse['Scenario 2']['2030']['Operation cost (EUR-million/year)'] : ''}</td>
            <td>{jsonResponse['Scenario 2']['2040'] ? jsonResponse['Scenario 2']['2040']['Operation cost (EUR-million/year)'] : ''}</td>
          </tr>
        </tbody>
      </Table>
    </div>
  );
};

export default CostPerYear;
