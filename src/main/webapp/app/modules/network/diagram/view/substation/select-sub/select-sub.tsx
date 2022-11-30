import React from 'react';
import Select from 'react-select';
import { FixedSizeGrid } from 'react-window';

const customSelectStyles = {
  container: provided => ({
    ...provided,
    position: 'absolute',
    right: 20,
    zIndex: 1200,
    top: 125,
  }),
  control: provided => ({
    ...provided,
    width: 200,
    float: 'right',
  }),
  menu: provided => ({
    ...provided,
    width: 200,
    right: 0,
  }),
  option: (provided, state) => ({
    ...provided,
    color: state.isSelected ? 'white' : 'black',
  }),
};

const SelectSub = React.forwardRef<FixedSizeGrid, any>((props: any, ref: FixedSizeGrid) => {
  const { network, indexesMap } = props;

  const [selectedOption, setSelectedOption] = React.useState(null);

  const getIndex = substation => {
    const sudIds = [...network];
    return sudIds.indexOf(substation.value);
  };

  const focusOnCurrentSVG = substation => {
    setSelectedOption(substation);
    const index = getIndex(substation);
    // first focus on element
    ref.current.scrollToItem({
      rowIndex: indexesMap.get(index).x,
      columnIndex: indexesMap.get(index).y,
    });

    // then highlight
    setTimeout(function () {
      const subId = substation.value;
      const element = document.getElementById(subId + '_parent');
      const parent = element.parentElement;
      parent.style.transition = 'background-color .5s ease';
      parent.classList.toggle('svg-bg');
      setTimeout(function () {
        parent.classList.toggle('svg-bg');
      }, 1000);
    }, 1000);
  };

  const getOptions = () => {
    const subIds = [...network];
    return subIds.map(subId => ({ value: subId, label: subId }));
  };

  return (
    <>
      <Select onChange={focusOnCurrentSVG} value={selectedOption} options={getOptions()} styles={customSelectStyles} />
    </>
  );
});

export default SelectSub;
