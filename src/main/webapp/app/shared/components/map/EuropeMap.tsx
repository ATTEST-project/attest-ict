// src/components/EuropeMap.js
import React from 'react';
import { MapContainer, TileLayer, Marker, Popup, GeoJSON, Tooltip } from 'react-leaflet';
import L from 'leaflet';
import './EuropeMap.scss';
import europeGeoJSON from '../../../../content/map/europe.geo.json'; // Path to your GeoJSON file

const EuropeMap = ({ highlightedCountries }) => {
  const customIcon = new L.Icon({
    html: 'Hello',
    iconUrl: '../../../../content/map/power_network.png', // Replace with the path to your custom icon
    iconSize: [32, 32], // Set the icon size
    iconAnchor: [16, 32], // Set the anchor point (usually half of iconSize)
  });

  const setColor = ({ properties }) => {
    return { weight: 1 };
  };

  return (
    <div className="europe-map">
      <MapContainer
        center={[49.35, 12.11]}
        zoom={3.5}
        style={{ position: 'absolute', width: '100%', height: '100%' }}
        zoomControl={false}
        dragging={false}
        scrollWheelZoom={false}
        touchZoom={false}
        doubleClickZoom={false}
        boxZoom={false}
        keyboard={false}
        maxBoundsViscosity={1.0}
      >
        {/* <TileLayer*/}
        {/*  url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"*/}
        {/*  attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'*/}
        {/* />*/}
        <GeoJSON data={europeGeoJSON} style={setColor} />

        {/* Loop through GeoJSON features to render markers */}
        {europeGeoJSON.features.map(feature => {
          const countryName = feature.properties.ISO2;
          const isHighlighted = highlightedCountries.includes(countryName);

          if (isHighlighted) {
            return (
              <Marker key={countryName} position={[feature.properties.LAT, feature.properties.LON]} icon={customIcon}>
                {/* <Popup>{countryName}</Popup> */}
                <Tooltip permanent={true} direction="center" offset={[0, 5]} className="my-labels">
                  {feature.properties.ISO2}
                </Tooltip>
              </Marker>
            );
          }
          return null;
        })}
      </MapContainer>
    </div>
  );
};

export default EuropeMap;
