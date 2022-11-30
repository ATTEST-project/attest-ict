import dayjs from 'dayjs';

import { APP_LOCAL_DATETIME_FORMAT } from 'app/config/constants';

export const convertDateTimeFromServer = date => (date ? dayjs(date).format(APP_LOCAL_DATETIME_FORMAT) : null);

export const convertDateTimeToServer = date => (date ? dayjs(date).toDate() : null);

export const displayDefaultDateTime = () => dayjs().startOf('day').format(APP_LOCAL_DATETIME_FORMAT);

export const getSeasonFromDate = (date: string | Date) => {
  let dateToCheck;
  if (typeof date === 'string') {
    dateToCheck = new Date(date);
  } else {
    dateToCheck = date;
  }

  const month = dateToCheck.getMonth() + 1;

  if (month > 3 && month < 6) {
    return 'spring';
  }

  if (month > 6 && month < 9) {
    return 'summer';
  }

  if (month > 9 && month < 12) {
    return 'autumn';
  }

  if (month >= 1 && month < 3) {
    return 'winter';
  }

  const day = dateToCheck.getDate();
  if (month === 3) {
    return day < 22 ? 'winter' : 'spring';
  }

  if (month === 6) {
    return day < 22 ? 'spring' : 'summer';
  }

  if (month === 9) {
    return day < 22 ? 'summer' : 'autumn';
  }

  if (month === 12) {
    return day < 22 ? 'autumn' : 'winter';
  }

  console.error('Unable to calculate current season');
};

export const isWeekend = (date: string | Date) => {
  let dateToCheck;
  if (typeof date === 'string') {
    dateToCheck = new Date(date);
  } else {
    dateToCheck = date;
  }

  return dateToCheck.getDay() === 6 || dateToCheck.getDay() === 0;
};

export const getTypeOfDay = (date: string | Date) => {
  let dateToCheck;
  if (typeof date === 'string') {
    dateToCheck = new Date(date);
  } else {
    dateToCheck = date;
  }

  switch (dateToCheck.getDay()) {
    case 0:
      return 'Sunday';
    case 6:
      return 'Saturday';
    default:
      return 'Business Day';
  }
};
