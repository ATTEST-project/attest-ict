import React from 'react';
import InputRow from 'app/modules/tools/WP5/T52/input-row/input-row';
import Divider from 'app/shared/components/divider/divider';

const Config = () => {
  const [nRows, setNRows] = React.useState<number>(1);
  const callbackNRows = (value: number) => setNRows(prevState => prevState + value);

  return (
    <div className="section-with-border">
      {[...Array(nRows).keys()].map((e, i) => (
        <div key={i}>
          <InputRow index={i} nRows={nRows} callbackNRows={callbackNRows} />
          {nRows > 1 && <Divider />}
        </div>
      ))}
    </div>
  );
};

export default Config;
