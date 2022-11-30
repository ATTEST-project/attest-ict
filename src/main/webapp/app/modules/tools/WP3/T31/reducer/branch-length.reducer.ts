import { createAsyncThunk, isFulfilled, isPending } from '@reduxjs/toolkit';
import axios from 'axios';
import { createEntitySlice } from 'app/shared/reducers/reducer.utils';

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: null,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

const apiBranchLengthUrl = 'api/branch-extensions/network';

export const getEntitiesByNetworkId = createAsyncThunk('branch_length/fetch_entity_list', async (networkId: string | number) => {
  const requestUrl = `${apiBranchLengthUrl}?networkId=${networkId}&length=0`;
  return axios.get<any>(requestUrl);
});

export const BranchLengthSlice = createEntitySlice({
  name: 'branch_length',
  initialState,
  extraReducers(builder) {
    builder
      .addMatcher(isFulfilled(getEntitiesByNetworkId), (state, action) => {
        return {
          ...state,
          loading: false,
          updating: false,
          updateSuccess: true,
          entities: action.payload.data,
        };
      })
      .addMatcher(isPending(getEntitiesByNetworkId), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      });
  },
});

export const { reset } = BranchLengthSlice.actions;

export default BranchLengthSlice.reducer;
