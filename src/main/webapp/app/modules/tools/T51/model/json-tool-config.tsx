export interface JsonToolConfig {
  config: string;
  path: string;
  selectedVariables: string;
  variables: string[];
  component1_field: string;
  components: [];
  method: string;
  results: string[];
  path2: string;
  variables2: string;
  components2: [];
  component2_field: string;
  assestsType: string;
}

export const defaultValue: Readonly<JsonToolConfig> = {
  assestsType: '',
  component1_field: '',
  component2_field: '',
  components: [],
  components2: [],
  config: '',
  method: '',
  path: '',
  path2: '',
  results: [],
  selectedVariables: '',
  variables: [],
  variables2: '',
};
