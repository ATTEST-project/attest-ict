import React from 'react';
import NetworkSearch from 'app/shared/components/network-search/network-search';

const Search = props => {
  const [rowsSelected, setRowsSelected] = React.useState([]);
  const setRowsSelectedCallback = (rows: any) => setRowsSelected(rows);

  return (
    <>
      <h4>{'Search networks'}</h4>
      <NetworkSearch setRowsSelectedCallback={setRowsSelectedCallback} />
      {rowsSelected.length > 0 && (
        <>
          <div style={{ marginTop: 20 }}>Outside Network Search component</div>
          <div>Rows selected: </div>
          {rowsSelected.map((row, index) => (
            <div key={index}>{row.name}</div>
          ))}
        </>
      )}
    </>
  );
};

export default Search;
