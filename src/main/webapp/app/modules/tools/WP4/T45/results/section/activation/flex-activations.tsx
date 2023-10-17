import React from 'react';
import { Table } from 'reactstrap';
import Divider from 'app/shared/components/divider/divider';

interface FlexActivationsInterface {
  flexActivations: any;
  title: string;
}

const FlexActivations = (props: FlexActivationsInterface) => {
  const { flexActivations, title } = props;

  return (
    <>
      <div className="section-with-border">
        <div>{title}</div>
        <Table>
          <thead>
            <tr>
              <th>Active Power Up Tot</th>
              <th>Active Power Dn Tot</th>
              <th>Reactive Power Up Tot</th>
              <th>Reactive Power Dn Tot</th>
            </tr>
          </thead>
          <tbody>
            {flexActivations?.map((flexActivation, index) => (
              <tr key={index}>
                <td> {flexActivation.activePowerUpTot} </td>
                <td> {flexActivation.activePowerDnTot} </td>
                <td> {flexActivation.reactivePowerUpTot} </td>
                <td> {flexActivation.reactivePowerDnTot} </td>
              </tr>
            ))}
          </tbody>
        </Table>
      </div>

      <Divider />
    </>
  );
};

export default FlexActivations;
