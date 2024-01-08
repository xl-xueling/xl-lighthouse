import { useContext } from 'react';
import { GlobalContext } from '@/context';
import defaultLocale from '../locale/index';
import commonLocale from '../locale/common';

function useLocale(locale = null) {
  const { lang } = useContext(GlobalContext);
  const customLocale =  (locale || defaultLocale)[lang] || {};
  return Object.assign(customLocale,commonLocale[lang]);
}

export default useLocale;
