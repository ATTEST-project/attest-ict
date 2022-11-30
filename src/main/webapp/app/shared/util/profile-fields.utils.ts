export const getSeason = (season: string) => {
  const s = season.toLowerCase();
  switch (s) {
    case 'a':
      return 'Autumn';
    case 's':
      return 'Spring';
    case 'su':
      return 'Summer';
    case 'w':
      return 'Winter';
    default:
      return '';
  }
};

export const getTypicalDay = (typicalDay: string) => {
  const td = typicalDay.toLowerCase();
  switch (td) {
    case 'bd':
      return 'Business Day';
    case 'sa':
      return 'Saturday';
    case 'su':
      return 'Sunday';
    default:
      return '';
  }
};

export const getMode = (mode: number) => {
  switch (mode) {
    case 1:
      return 'Year time-series';
    case 2:
      return 'Business day for a season';
    case 3:
      return 'Business day for a month';
    case 4:
      return 'Weekend for a season';
    case 5:
      return 'Weekend for a month';
    default:
      return '';
  }
};

export const getTimeInterval = (timeInterval: number) => {
  switch (timeInterval) {
    case 1:
      return '1 hour';
    case 0.5:
      return '30 minutes';
    case 0.25:
      return '15 minutes';
    default:
      return '';
  }
};
