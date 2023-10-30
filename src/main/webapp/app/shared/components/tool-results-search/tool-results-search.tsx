import React from 'react';
import { Button, Col, Collapse, Form, Row, Spinner } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { ValidatedField } from 'react-jhipster';
import { FormProvider, useForm } from 'react-hook-form';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { ITool } from 'app/shared/model/tool.model';
import { IToolResult } from 'app/shared/model/tool-results.model';
import { defaultValue as filterDefaultValue, IToolResultsFilter } from 'app/shared/model/tool-results-filter.model';
import { isStringNotUndefinedNotNullNotEmpty } from 'app/shared/util/string-utils';
import { SELECTION_TYPE } from 'app/shared/components/network-search/constants/constants';
import { getToolByNum, getToolResults, getTools, reset } from 'app/shared/reducers/tool-results-search';
import SectionHeader from 'app/shared/components/section-header/section-header';
import ToolResults from 'app/shared/components/tool-results-search/tool-results';

const ToolResultsSearch = (props: any) => {
  const { network, setRowsSelectedCallback, selectionType, toolNum, filtersBy } = props;
  // list of the ATTEST's tools
  const dispatch = useAppDispatch();
  const toolListLoading = useAppSelector(state => state.toolResultsSearch.loading);
  const toolList = useAppSelector(state => state.toolResultsSearch.toolList);
  const [showResults, setShowResults] = React.useState<boolean>(false);
  const [defaultToolId, setDefaultToolId] = React.useState(null);

  React.useEffect(() => {
    if (isStringNotUndefinedNotNullNotEmpty(toolNum)) {
      dispatch(getToolByNum(toolNum)); // get tool identified by tollNum (api/tools?num.equals=toolNum)
    } else {
      dispatch(getTools()); // api/tools? // get list of all tool
    }
  }, []);

  React.useEffect(() => {
    if (!toolList) {
      return;
    }
    if (toolList.length === 1) {
      setDefaultToolId(toolList[0].id.toString());
    }
  }, [toolList]);

  React.useEffect(() => {
    if (!defaultToolId) {
      return;
    }
    // eslint-disable-next-line no-console
    // console.log('Tool-results- useEffect() 3 - defaultToolId:  ', defaultToolId);
    setValue('toolId', defaultToolId);
  }, [defaultToolId]);

  const methods = useForm();

  const {
    handleSubmit,
    register,
    formState: { errors },
    reset,
    setValue,
  } = methods;

  const [toolResults, setToolResults] = React.useState<IToolResult[]>(null);
  const [loading, setLoading] = React.useState<boolean>(false);

  const submitForm = data => {
    /* eslint-disable-next-line no-console */
    console.log('Tool-results- search - submitForm() - data: ', data);
    const fileNameContain = data.fileName;
    const toolId = data.toolId;
    const dateTimeEnd = data.dateTimeEnd ? new Date(data.dateTimeEnd).toISOString() : null;
    const networkId = network.id;
    setLoading(true);
    setToolResults(null);
    setShowResults(true);
    dispatch(getToolResults({ networkId, toolId, dateTimeEnd, fileName: fileNameContain }))
      .unwrap()
      .then(res => {
        setLoading(false);
        setToolResults(res.data);
      })
      .catch(err => {
        setLoading(false);
        setToolResults(null);
      });
  };

  const resetRow = () => {
    reset({ ...filterDefaultValue });
    props.setRowsSelectedCallback([]);
    setToolResults(null);
    setShowResults(true);
  };

  const singleToolSelection = () => {
    return (
      <Col>
        <ValidatedField
          register={register}
          error={errors?.toolId}
          id={'tool-select'}
          label="Tool"
          name="toolId"
          data-cy="toolId"
          type="select"
          value={defaultToolId}
          validate={{ required: { value: true, message: 'Tool selection is a required..' } }}
        >
          <option value="" hidden>
            Select Tool ...
          </option>
          {toolList?.map((tool: any) => (
            <option key={tool.id} value={tool.id}>
              {' '}
              {tool.num}
              {'  '}
            </option>
          ))}
        </ValidatedField>
      </Col>
    );
  };

  const multipleToolsSelection = () => {
    return (
      <Col>
        <ValidatedField
          register={register}
          error={errors?.toolId}
          id={'tool-select'}
          label="Tool"
          name="toolId"
          data-cy="toolId"
          type="select"
          validate={{ required: { value: true, message: 'Tool selection is a required..' } }}
        >
          <option value="" hidden>
            Select Tool ...
          </option>
          {toolList?.map((tool: any) => (
            <option key={tool.id} value={tool.id}>
              {' '}
              {tool.num}
              {'  '}
            </option>
          ))}
        </ValidatedField>
      </Col>
    );
  };

  return (
    <div style={{ border: '1px solid white', borderRadius: 10, padding: 10 }}>
      <SectionHeader title="Search for Tool Execution Results" />

      <FormProvider {...methods}>
        <Form onSubmit={handleSubmit(submitForm)}>
          <Row md="7">
            {/* Check whether toolList has only one element */}
            {toolList && toolList.length === 1 ? singleToolSelection() : multipleToolsSelection()}

            {!filtersBy && (
              <Col>
                <ValidatedField
                  register={register}
                  error={errors?.fileName}
                  id={'file-name-input'}
                  label="fileName contains"
                  name="fileName"
                  data-cy="fileName"
                  type="text"
                  placeholder="FileName... (optional)"
                />
              </Col>
            )}
            <Col>
              <ValidatedField
                id="task-date"
                register={register}
                error={errors?.dateTimeEnd}
                label="Task End Date Time "
                name="dateTimeEnd"
                data-cy="dateTimeEnd"
                type="datetime-local"
              />
            </Col>

            <Col md="2" style={{ alignSelf: 'end' }}>
              <div className="mb-3" style={{ display: 'flex', justifyContent: 'space-between' }}>
                <Button id="upload-button" color="primary" type="submit">
                  {toolListLoading ? (
                    <Spinner color="light" size="sm" />
                  ) : (
                    <>
                      <FontAwesomeIcon icon="search" />
                      <span>{' Search'}</span>
                    </>
                  )}
                </Button>
                <div onClick={resetRow} style={{ cursor: 'pointer', padding: 10 }}>
                  <FontAwesomeIcon icon="times" />
                  {' Remove Filters'}
                </div>
              </div>
            </Col>
            {toolResults && (
              <Col style={{ alignSelf: 'end', textAlign: 'right' }}>
                <div className="mb-3">
                  <Button onClick={() => setShowResults(!showResults)} color="dark">
                    <FontAwesomeIcon icon="angle-down" style={showResults && { transform: 'rotate(-180deg)' }} />
                  </Button>
                </div>
              </Col>
            )}
          </Row>
        </Form>
      </FormProvider>

      <div>
        <Collapse isOpen={showResults}>
          {toolResults && (
            <ToolResults
              setRowsSelectedCallback={props.setRowsSelectedCallback}
              selectionType={props.selectionType}
              filtersBy={filtersBy}
            />
          )}
        </Collapse>
      </div>
    </div>
  );
};

export default ToolResultsSearch;
