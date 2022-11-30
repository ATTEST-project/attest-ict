export interface INode {
  id?: number;
  networkId?: string | null;
  loadId?: number | null;
  name?: string | null;
}

export const defaultValue: Readonly<INode> = {};
