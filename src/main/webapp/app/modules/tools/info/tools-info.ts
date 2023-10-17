import { WP_IMAGES } from 'app/shared/util/wp-image-constant';

const toolsInfo = {
  WP2: [
    {
      name: 'T2.5',
      description: 'Day Ahead Aggregator tool',
      to: '/tools/t251',
      // type: 'all',
      supportedNetworkType: 'ALL',
      active: true, // ex ready
    },
    {
      name: 'T2.5',
      description: 'Real Time Optimisation Tool',
      to: '/tools/t252',
      supportedNetworkType: 'ALL',
      active: true, // ex ready
    },
    {
      name: 'T2.6',
      description: 'Market Simulator',
      to: '/tools/t26',
      supportedNetworkType: 'ALL',
      active: true, // ex ready
    },
  ],
  WP3: [
    {
      name: 'T3.1',
      description: 'Optimisation Tool for distribution network planning',
      to: '/tools/t31',
      supportedNetworkType: 'DX',
      active: true, // ex ready
    },
    {
      name: 'T3.2',
      description: 'Optimisation Tool for transmission network planning',
      to: '/tools/t32',
      supportedNetworkType: 'TX',
      active: true, // ex ready
    },
    {
      name: 'T3.3',
      description: 'Optimisation tool for planning TSO/DSO shared technologies',
      to: '/tools/t33',
      supportedNetworkType: 'ALL',
      active: true, // ex ready
    },
  ],
  WP4: [
    {
      name: 'TSG',
      description: 'Scenario Generation Tool',
      to: '/tools/sgt',
      supportedNetworkType: 'ALL',
      active: true,
    },
    {
      name: 'T4.1',
      description: 'Tool for ancillary services activation in day ahead (DX)',
      to: '/tools/t41',
      supportedNetworkType: 'DX',
      active: true,
    },
    {
      name: 'T4.2',
      description: 'Tool for ancillary services activation in real time (DX)',
      to: '/tools/t42',
      supportedNetworkType: 'DX',
      active: true,
    },
    {
      name: 'T4.4',
      description: 'Tool for ancillary services activation in day ahead (TX)',
      to: '/tools/t44',
      supportedNetworkType: 'TX',
      active: true,
    },
    {
      name: 'T4.5',
      description: 'Tool for ancillary services activation in real time (TX)',
      to: '/tools/t45',
      supportedNetworkType: 'TX',
      active: true,
    },
  ],
  WP5: [
    {
      name: 'T5.1',
      description: 'Assets Characterization and Monitoring Tools',
      to: '/tools/t51',
      supportedNetworkType: 'ALL',
      active: true,
    },
    {
      name: 'T5.2',
      description: 'Tool for the definition of condition indicators',
      to: '/tools/t52',
      supportedNetworkType: 'ALL',
      active: true,
    },
    {
      name: 'T5.3',
      description: 'Tool for the definition of smart asset management strategies',
      to: '/tools/t53',
      supportedNetworkType: 'ALL',
      active: true,
    },
  ],
};

export default toolsInfo;

export const WP_IMAGE = {
  WP3: {
    src: WP_IMAGES[0].src,
    alt: WP_IMAGES[0].altText,
  },
  WP4: {
    src: WP_IMAGES[1].src,
    alt: WP_IMAGES[1].altText,
  },
  WP5: {
    src: WP_IMAGES[2].src,
    alt: WP_IMAGES[2].altText,
  },
  WP2: {
    src: WP_IMAGES[3].src,
    alt: WP_IMAGES[3].altText,
  },
};
