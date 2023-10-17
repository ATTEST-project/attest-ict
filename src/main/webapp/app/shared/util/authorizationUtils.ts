import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { AUTHORITIES } from 'app/config/constants';
import { INetwork } from 'app/shared/model/network.model';

export function isUserDSO(authorities: string[]): boolean {
  const hasDsoAuth = hasAnyAuthority(authorities, [AUTHORITIES.DSO]);
  const hasTsoAuth = hasAnyAuthority(authorities, [AUTHORITIES.TSO]);
  const hasAdminAuth = hasAnyAuthority(authorities, [AUTHORITIES.ADMIN]);

  return hasDsoAuth && !hasAdminAuth && !hasTsoAuth;
}

export function isUserTSO(authorities: string[]): boolean {
  const hasDsoAuth = hasAnyAuthority(authorities, [AUTHORITIES.DSO]);
  const hasTsoAuth = hasAnyAuthority(authorities, [AUTHORITIES.TSO]);
  const hasAdminAuth = hasAnyAuthority(authorities, [AUTHORITIES.ADMIN]);

  return hasTsoAuth && !hasAdminAuth && !hasDsoAuth;
}

export function isUserTSOAndDSO(authorities: string[]): boolean {
  const hasDsoAuth = hasAnyAuthority(authorities, [AUTHORITIES.DSO]);
  const hasTsoAuth = hasAnyAuthority(authorities, [AUTHORITIES.TSO]);
  const hasAdminAuth = hasAnyAuthority(authorities, [AUTHORITIES.ADMIN]);
  return hasTsoAuth && hasDsoAuth && !hasAdminAuth;
}

export function isUserAdmin(authorities: string[]): boolean {
  const hasAdminAuth = hasAnyAuthority(authorities, [AUTHORITIES.ADMIN]);
  return hasAdminAuth;
}

export function shouldShowToolCard(authorities: string[], supportedNetworkType: string): boolean {
  const hasDsoAuth = hasAnyAuthority(authorities, [AUTHORITIES.DSO]);
  const hasTsoAuth = hasAnyAuthority(authorities, [AUTHORITIES.TSO]);
  const hasAdminAuth = hasAnyAuthority(authorities, [AUTHORITIES.ADMIN]);

  switch (supportedNetworkType.toUpperCase()) {
    case 'ALL':
      return hasDsoAuth || hasTsoAuth || hasAdminAuth;
    case 'DX':
      return hasDsoAuth || hasAdminAuth;
    case 'TX':
      return hasTsoAuth || hasAdminAuth;
    default:
      return false;
  }
}

export function shouldShowButtonConfiguredAndRun(
  authorities: string[],
  toolSupportedNetworkType: string,
  isToolIntegrated: boolean,
  selectedNetwork: INetwork
): boolean {
  // no network selected or tool not integrated yet
  if (!selectedNetwork || !isToolIntegrated) {
    return false;
  }

  const hasDsoAuth = hasAnyAuthority(authorities, [AUTHORITIES.DSO]);
  const hasTsoAuth = hasAnyAuthority(authorities, [AUTHORITIES.TSO]);
  const hasAdminAuth = hasAnyAuthority(authorities, [AUTHORITIES.ADMIN]);

  switch (toolSupportedNetworkType.toUpperCase()) {
    case 'ALL':
      return hasDsoAuth || hasTsoAuth || hasAdminAuth;
    case 'DX':
      return selectedNetwork.type.toUpperCase() === toolSupportedNetworkType.toUpperCase() && (hasDsoAuth || hasAdminAuth);
    case 'TX':
      return selectedNetwork.type.toUpperCase() === toolSupportedNetworkType.toUpperCase() && (hasTsoAuth || hasAdminAuth);
    default:
      return false;
  }
}

export function generateNetworkTypeOptions(authorities: string[]) {
  const isDso = isUserDSO(authorities);
  const isTso = isUserTSO(authorities);

  const optionsMap = {
    ALL: [
      { key: 'DX', value: 'DX', label: 'Distribution' },
      { key: 'TX', value: 'TX', label: 'Transmission' },
    ],
    TX: [{ key: 'TX', value: 'TX', label: 'Transmission' }],
    DX: [{ key: 'DX', value: 'DX', label: 'Distribution' }],
  };

  // user is DSO
  if (isDso) {
    return optionsMap['DX'];
  }

  // user is TSO
  if (isTso) {
    return optionsMap['TX'];
  }

  // user is ADMIN or ()TSO and also DSO)
  return optionsMap['ALL'];
}

// Only admin user can insert, delete or update entity data
export function hasCrudOperationsAuthority(authorities: string[]): boolean {
  return isUserAdmin(authorities);
}

// TSO, DSO and Admin User can edit network data, both created by them and those created by other users
export function hasAuthorityForModifyNetworkData(authorities: string[]): boolean {
  const hasDsoAuth = hasAnyAuthority(authorities, [AUTHORITIES.DSO]);
  const hasTsoAuth = hasAnyAuthority(authorities, [AUTHORITIES.TSO]);
  const hasAdminAuth = hasAnyAuthority(authorities, [AUTHORITIES.ADMIN]);
  return hasDsoAuth || hasTsoAuth || hasAdminAuth;
}

// Only user with role ADMIN can update Task
export function hasAuthorityForUpdateTask(authorities: string[]): boolean {
  return isUserAdmin(authorities);
}

// Only user with role ADMIN can list all task
export function shouldFilterTask(authorities: string[]): boolean {
  const hasAdminAuth = hasAnyAuthority(authorities, [AUTHORITIES.ADMIN]);
  return !hasAdminAuth;
}

// Only user with role ADMIN can update Simulation
export function hasAuthorityForUpdateSimulation(authorities: string[]): boolean {
  return isUserAdmin(authorities);
}
