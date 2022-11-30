import { createAsyncThunk, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';
import axios from 'axios';
import { getEntities } from 'app/entities/network/network.reducer';
import { createEntitySlice, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { SECTION } from 'app/shared/util/file-utils';

interface ProfileParams {
  type?: string;
  mode: number;
  season?: string;
  typicalDay: string;
  file: File;
  networkId: number;
  isTX?: boolean;
}

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: {},
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

const apiGenProfileUrl = '/api/excel/gen-profile';
const apiLoadProfileUrl = '/api/excel/load-profile';
const apiFlexProfileUrl = '/api/excel/flex-profile';
const apiProfileTXUrl = '/api/excel/all-profile';

const getUrlByType = (type: string) => {
  switch (type) {
    case SECTION.GENERATOR:
      return apiGenProfileUrl;
    case SECTION.LOAD:
      return apiLoadProfileUrl;
    case SECTION.FLEXIBILITY:
      return apiFlexProfileUrl;
    default:
      throw new Error('Type not valid!');
  }
};

const createFormData = ({ mode, season, typicalDay, file, networkId }) => {
  const formData = new FormData();
  formData.append('mode', '' + mode);
  season && formData.append('season', season);
  formData.append('typicalDay', typicalDay);
  formData.append('file', file);
  formData.append('networkId', '' + networkId);
  return formData;
};

export const uploadExcelProfile = createAsyncThunk(
  'network_upload_profile',
  async (props: ProfileParams, thunkAPI) => {
    const { type, mode, season, typicalDay, file, networkId, isTX } = props;
    const requestUrl = isTX ? apiProfileTXUrl : getUrlByType(type);
    const formData = createFormData({ mode, season, typicalDay, file, networkId });
    const result = await axios.post(requestUrl, formData, {
      headers: { 'content-type': 'multipart/form-data' },
    });
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const NetworkExcelProfileSlice = createEntitySlice({
  name: 'network_csv_profile',
  initialState,
  extraReducers(builder) {
    builder
      .addMatcher(isPending(uploadExcelProfile), state => {
        state.loading = true;
      })
      .addMatcher(isFulfilled(uploadExcelProfile), (state, action) => {
        return {
          ...state,
          loading: false,
          entity: action.payload.data,
        };
      })
      .addMatcher(isRejected(uploadExcelProfile), state => {
        state.loading = false;
        state.entity = {};
        state.errorMessage = 'Error uploading csv profile file';
      });
  },
});

export const { reset } = NetworkExcelProfileSlice.actions;

export default NetworkExcelProfileSlice.reducer;
