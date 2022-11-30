import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  substation: '',
};

const ChooseSubstationSlice = createSlice({
  name: 'choose_substation',
  initialState,
  reducers: {
    chooseSubstation(state, action) {
      state.substation = action.payload;
    },
  },
});

export const { chooseSubstation } = ChooseSubstationSlice.actions;

export default ChooseSubstationSlice.reducer;
