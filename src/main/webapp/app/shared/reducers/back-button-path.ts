import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  path: '',
};

const BackButtonPathSlice = createSlice({
  name: 'back_button_display',
  initialState,
  reducers: {
    pathButton(state, action) {
      state.path = action.payload;
    },
  },
});

export const { pathButton } = BackButtonPathSlice.actions;

export default BackButtonPathSlice.reducer;
