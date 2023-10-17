export const getToolNum = (toolTitle: string | null): string | null => {
  if (toolTitle === null) {
    return '';
  }
  const parts = toolTitle.split('-');
  if (parts.length > 0) {
    return parts[0].trim();
  } else {
    return '';
  }
};
