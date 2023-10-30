import React from 'react';
import { IToolResult } from 'app/shared/model/tool-results.model';

interface ToolResultsInterface {
  toolResults: IToolResult[];
  title: string;
}

export const PrintResult = (props: ToolResultsInterface | undefined) => {
  const results = props.toolResults;
  const title = props.title;

  return (
    <>
      <span>{title}</span>
      {results?.map((res, index) => (
        <div key={index}>
          {' '}
          <span> {res.fileName} </span>{' '}
        </div>
      ))}
    </>
  );
};
