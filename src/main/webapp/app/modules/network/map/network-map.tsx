import React, { useEffect } from 'react';
import { MapContainer, Circle, TileLayer, Polyline, LayersControl, Popup, FeatureGroup } from 'react-leaflet';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity } from 'app/entities/network/network.reducer';
import { RouteComponentProps } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Button } from 'reactstrap';

function NetworkMap(props: RouteComponentProps<{ id: string }>) {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const networkEntity = useAppSelector(state => state.network.entity);

  const position: [number, number] = [46.16362413710777, 16.83148937648012];

  const subst1: [number, number] = [46.159107536926975, 16.826187787188744];
  const subst2: [number, number] = [46.18829667306288, 16.8192298907159];
  const subst3: [number, number] = [46.13866581458786, 16.803004113476938];
  const subst4: [number, number] = [46.157694015411515, 16.87046703107362];
  const subst5: [number, number] = [46.17433829531046, 16.77639660144771];
  const subst6: [number, number] = [46.14211516405945, 16.85089763512954];

  const polyline1 = [subst2, subst5];
  const polyline2 = [subst6, subst4];
  const polyline3 = [subst2, subst4];
  const polyline4 = [subst1, subst5];
  const polyline5 = [subst6, subst3];
  const polyline6 = [subst3, subst5];

  const substRadius = 100;
  const substColor = 'red';
  const lineOptions = { color: 'blue', weight: 4 };

  const handleGoBack = () => {
    props.history.push('/network');
  };

  return (
    <div>
      <h4>{'Map of network ' + networkEntity.name}</h4>
      <MapContainer className="map" center={position} zoom={13} style={{ height: 720, width: '80%' }}>
        <LayersControl position="topright">
          <LayersControl.BaseLayer checked name="OpenStreetMap.Mapnik">
            <TileLayer
              attribution='&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
              url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
            />
          </LayersControl.BaseLayer>
          <LayersControl.BaseLayer name="OpenStreetMap.BlackAndWhite">
            <TileLayer
              attribution='&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
              url="https://tiles.wmflabs.org/bw-mapnik/{z}/{x}/{y}.png"
            />
          </LayersControl.BaseLayer>

          <LayersControl.Overlay checked name="SUBST19-20-21">
            <FeatureGroup pathOptions={{ color: substColor }}>
              <Popup>Substation: SUBST19-20-21</Popup>
              <Circle center={subst1} radius={substRadius} />
            </FeatureGroup>
          </LayersControl.Overlay>

          <LayersControl.Overlay checked name="SUBST36">
            <FeatureGroup pathOptions={{ color: substColor }}>
              <Popup>Substation: SUBST36</Popup>
              <Circle center={subst2} radius={substRadius} />
            </FeatureGroup>
          </LayersControl.Overlay>

          <LayersControl.Overlay checked name="SUBST29-30">
            <FeatureGroup pathOptions={{ color: substColor }}>
              <Popup>Substation: SUBST29-30</Popup>
              <Circle center={subst3} radius={substRadius} />
            </FeatureGroup>
          </LayersControl.Overlay>

          <LayersControl.Overlay checked name="SUBST35-37-38">
            <FeatureGroup pathOptions={{ color: substColor }}>
              <Popup>Substation: SUBST35-37-38</Popup>
              <Circle center={subst4} radius={substRadius} />
            </FeatureGroup>
          </LayersControl.Overlay>

          <LayersControl.Overlay checked name="SUBST01-02">
            <FeatureGroup pathOptions={{ color: substColor }}>
              <Popup>Substation: SUBST01-02</Popup>
              <Circle center={subst5} radius={substRadius} />
            </FeatureGroup>
          </LayersControl.Overlay>

          <LayersControl.Overlay checked name="SUBST34">
            <FeatureGroup pathOptions={{ color: substColor }}>
              <Popup>Substation: SUBST34</Popup>
              <Circle center={subst6} radius={substRadius} />
            </FeatureGroup>
          </LayersControl.Overlay>

          <LayersControl.Overlay checked name="LINE01-36">
            <FeatureGroup>
              <Popup>Line: LINE01-36</Popup>
              <Polyline pathOptions={lineOptions} positions={polyline1} />
            </FeatureGroup>
          </LayersControl.Overlay>

          <LayersControl.Overlay checked name="LINE34-35">
            <FeatureGroup>
              <Popup>Line: LINE34-35</Popup>
              <Polyline pathOptions={lineOptions} positions={polyline2} />
            </FeatureGroup>
          </LayersControl.Overlay>

          <LayersControl.Overlay checked name="LINE35-36">
            <FeatureGroup>
              <Popup>Line: LINE35-36</Popup>
              <Polyline pathOptions={lineOptions} positions={polyline3} />
            </FeatureGroup>
          </LayersControl.Overlay>

          <LayersControl.Overlay checked name="LINE01-19">
            <FeatureGroup>
              <Popup>Line: LINE01-19</Popup>
              <Polyline pathOptions={lineOptions} positions={polyline4} />
            </FeatureGroup>
          </LayersControl.Overlay>

          <LayersControl.Overlay checked name="LINE29-34">
            <FeatureGroup>
              <Popup>Line: LINE29-34</Popup>
              <Polyline pathOptions={lineOptions} positions={polyline5} />
            </FeatureGroup>
          </LayersControl.Overlay>

          <LayersControl.Overlay checked name="LINE01-29">
            <FeatureGroup>
              <Popup>Line: LINE01-29</Popup>
              <Polyline pathOptions={lineOptions} positions={polyline6} />
            </FeatureGroup>
          </LayersControl.Overlay>
        </LayersControl>
      </MapContainer>
      <div style={{ marginTop: 20 }}>
        <Button onClick={handleGoBack} color="info" data-cy="entityUploadBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
      </div>
    </div>
  );
}

export default NetworkMap;
