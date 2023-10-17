export const defaultSeasonMap = [
  { key: 'W', value: 'Winter' },
  { key: 'Su', value: 'Summer' },
  // { key: 'A', value: 'Autumn' },
  // { key: 'S', value: 'Spring' },
];

export const defaultProfileMap = [
  { key: 1, value: 'Summer' },
  { key: 2, value: 'Winter' },
];

export const defaultT44ProblemsMap = [
  { key: 1, value: 'Contin Filtering' },
  { key: 2, value: 'AC-OPF' },
  { key: 3, value: 'AC-SCOPF' },
  { key: 4, value: 'Tractable S-MP-AC-SCOPF' },
  { key: 5, value: 'Security Assessment' },
  { key: 6, value: 'Power Flow' },
];

export const defaultFlexibilityMap = new Map<number, string>([
  [0, 'WithOut Flex'],
  [1, 'With Flex'],
]);
