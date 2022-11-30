import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  workPackage: '',
};

const ToolConfigSlice = createSlice({
  name: 'tool_config',
  initialState,
  reducers: {
    storeWP(state, action) {
      state.workPackage = action.payload;
    },
  },
});

export const { storeWP } = ToolConfigSlice.actions;

export default ToolConfigSlice.reducer;
