export function checkLogin() {
  return localStorage.getItem('userStatus') === 'login';
}

export function removeLoginStatus() {
  localStorage.removeItem('userStatus');
  localStorage.removeItem('loginParams');
  localStorage.removeItem('refreshKey');
  localStorage.removeItem('accessKey');
  localStorage.removeItem('userRole');
  localStorage.removeItem('cache_stared_metrics');
  localStorage.removeItem('cache_stared_projects');
  localStorage.removeItem('cache_all_department');
}