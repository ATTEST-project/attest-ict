const countryOptionsMap = {
  ALL: [
    { value: 'HR', label: 'Croatia' },
    { value: 'PT', label: 'Portugal' },
    { value: 'ES', label: 'Spain' },
    { value: 'UK', label: 'United Kingdom' },
  ],
};

export function generateCountryOptions() {
  return countryOptionsMap['ALL'];
}
