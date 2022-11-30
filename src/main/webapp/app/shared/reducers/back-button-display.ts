import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  display: true,
};

const BackButtonDisplaySlice = createSlice({
  name: 'back_button_display',
  initialState,
  reducers: {
    displayButton(state, action) {
      state.display = action.payload;
    },
  },
});

export const { displayButton } = BackButtonDisplaySlice.actions;

export default BackButtonDisplaySlice.reducer;
