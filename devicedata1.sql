CREATE TABLE mobiwaterDB.DeviceData(
recordid INT AUTO_INCREMENT,
imei VARCHAR(255) NOT NULL,
waterLevel VARCHAR(100) NOT NULL,
signalStrength VARCHAR(40) NOT NULL,
deviceType VARCHAR(100),
dateSavedOnDB TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY ( recordid )
);