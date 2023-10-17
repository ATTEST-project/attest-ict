export const isStringEmptyOrNullOrUndefined = (value: string | undefined | null): boolean =>
  value === undefined || value === null || value.trim() === '';

export const isStringNotUndefinedNotNullNotEmpty = (value: string | undefined | null): boolean =>
  value !== undefined && value !== null && value.trim() !== '';

export const addSpaceAfterComma = (description: string): string => {
  if (!description) return '';
  return description.replace(/,/g, ', '); // replaceAllComma present in description string with space
};
