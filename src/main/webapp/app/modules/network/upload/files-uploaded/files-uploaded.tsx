import React from 'react';
import { Button, Collapse, Modal, ModalBody, ModalFooter, ModalHeader, Spinner, Table } from 'reactstrap';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getSeason, getTypicalDay, getMode, getTimeInterval } from 'app/shared/util/profile-fields.utils';
import { SECTION } from 'app/shared/util/file-utils';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { deleteEntityAndRefresh } from 'app/entities/input-file/input-file.reducer';

const FilesUploaded = props => {
  const dispatch = useAppDispatch();

  const [openCollapse, setOpenCollapse] = React.useState(false);
  const [files, setFiles] = React.useState([]);

  const inputFileEntities = useAppSelector(state => state.inputFile.entities) || props.files;

  const [openModal, setOpenModal] = React.useState<boolean>(false);
  const [itemSelected, setItemSelected] = React.useState(null);
  const toggleModal = (item: any) => {
    setOpenModal(!openModal);
    setItemSelected(item);
  };

  React.useEffect(() => {
    if (!inputFileEntities) {
      return;
    }
    let files;
    if (props.isTx) {
      files = inputFileEntities.filter(file => file.description === SECTION.ALL);
    } else {
      files = inputFileEntities.filter(file => file.description === props.section);
    }
    if (files) {
      setFiles([...files]);
    }
  }, [inputFileEntities]);

  const [loading, setLoading] = React.useState(false);

  const deleteFile = () => {
    setLoading(true);
    dispatch(deleteEntityAndRefresh({ fileId: itemSelected.id, networkId: props.networkId }))
      .unwrap()
      .then(res => {
        setLoading(false);
        setOpenModal(false);
      })
      .catch(err => {
        setLoading(false);
      });
  };

  return (
    files.length > 0 && (
      <>
        <div>
          <Button onClick={() => setOpenCollapse(!openCollapse)} color="dark">
            {'Files Uploaded '}
            <FontAwesomeIcon icon="angle-down" style={openCollapse && { transform: 'rotate(-180deg)' }} />
          </Button>
        </div>
        <div style={{ flex: '0 0 100%' }}>
          <Collapse isOpen={openCollapse}>
            <Table>
              <thead>
                <tr>
                  <th>Season</th>
                  <th>Typical Day</th>
                  <th>Mode</th>
                  <th>Time Interval</th>
                  <th>Name</th>
                  <th>Delete</th>
                </tr>
              </thead>
              <tbody>
                {files.map((item, index) => (
                  <tr key={index}>
                    <td>{(item.season && getSeason(item.season)) || 'season_placeholder'}</td>
                    <td>{(item.typicalDay && getTypicalDay(item.typicalDay)) || 'typical-day_placeholder'}</td>
                    <td>{(item.mode && getMode(item.mode)) || 'mode_placeholder'}</td>
                    <td>{(item.timeInterval && getTimeInterval(item.timeInterval)) || 'time-interval_placeholder'}</td>
                    <td>{item.fileName}</td>
                    <td>
                      <Button color="dark" onClick={() => toggleModal(item)}>
                        <FontAwesomeIcon icon="trash" />
                      </Button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          </Collapse>
          <Modal isOpen={openModal}>
            <ModalHeader>Delete File</ModalHeader>
            <ModalBody>{'Are you sure you want to delete file ' + itemSelected?.fileName + '?'}</ModalBody>
            <ModalFooter>
              <Button color="secondary" onClick={toggleModal}>
                <FontAwesomeIcon icon="ban" />
                {' Cancel'}
              </Button>
              {!loading ? (
                <Button color="danger" onClick={deleteFile}>
                  <FontAwesomeIcon icon="trash" />
                  {' Delete'}
                </Button>
              ) : (
                <Button color="danger" disabled>
                  <Spinner color="light" size="sm" />
                </Button>
              )}
            </ModalFooter>
          </Modal>
        </div>
      </>
    )
  );
};

export default FilesUploaded;
