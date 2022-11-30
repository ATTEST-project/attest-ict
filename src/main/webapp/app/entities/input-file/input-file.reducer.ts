import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { IQueryParams, createEntitySlice, EntityState, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { IInputFile, defaultValue } from 'app/shared/model/input-file.model';

const initialState: EntityState<IInputFile> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

const apiUrl = 'api/input-files';
const apiByNetworkIdUrl = 'api/input-files-network';

// Actions

export const getEntities = createAsyncThunk('inputFile/fetch_entity_list', async ({ page, size, sort }: IQueryParams) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}&` : '?'}cacheBuster=${new Date().getTime()}`;
  return axios.get<IInputFile[]>(requestUrl);
});

export const getEntitiesByNetworkId = createAsyncThunk('inputFile/fetch_entity_list_by_network_id', async (networkId: string | number) => {
  return axios.get<IInputFile[]>(`${apiByNetworkIdUrl}/${networkId}`);
});

export const getEntity = createAsyncThunk(
  'inputFile/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<IInputFile>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const createEntity = createAsyncThunk(
  'inputFile/create_entity',
  async (entity: IInputFile, thunkAPI) => {
    const result = await axios.post<IInputFile>(apiUrl, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const updateEntity = createAsyncThunk(
  'inputFile/update_entity',
  async (entity: IInputFile, thunkAPI) => {
    const result = await axios.put<IInputFile>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const partialUpdateEntity = createAsyncThunk(
  'inputFile/partial_update_entity',
  async (entity: IInputFile, thunkAPI) => {
    const result = await axios.patch<IInputFile>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const deleteEntity = createAsyncThunk(
  'inputFile/delete_entity',
  async (id: string | number, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}`;
    const result = await axios.delete<IInputFile>(requestUrl);
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const deleteEntityAndRefresh = createAsyncThunk(
  'inputFile/delete_entity',
  async ({ fileId, networkId }: { fileId: number | string; networkId: number | string }, thunkAPI) => {
    const requestUrl = `${apiUrl}/${fileId}`;
    const result = await axios.delete<IInputFile>(requestUrl);
    thunkAPI.dispatch(getEntitiesByNetworkId(networkId));
    return result;
  },
  { serializeError: serializeAxiosError }
);

// slice

export const InputFileSlice = createEntitySlice({
  name: 'inputFile',
  initialState,
  extraReducers(builder) {
    builder
      .addCase(getEntity.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
      })
      .addCase(deleteEntity.fulfilled, state => {
        state.updating = false;
        state.updateSuccess = true;
        state.entity = {};
      })
      .addMatcher(isFulfilled(getEntities, getEntitiesByNetworkId), (state, action) => {
        return {
          ...state,
          loading: false,
          entities: action.payload.data,
          totalItems: parseInt(action.payload.headers['x-total-count'], 10),
        };
      })
      .addMatcher(isFulfilled(createEntity, updateEntity, partialUpdateEntity), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
      })
      .addMatcher(isPending(getEntities, getEntity, getEntitiesByNetworkId), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isPending(createEntity, updateEntity, partialUpdateEntity, deleteEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.updating = true;
      });
  },
});

export const { reset } = InputFileSlice.actions;

// Reducer
export default InputFileSlice.reducer;
