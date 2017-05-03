from django.conf.urls import include, url
from django.contrib import admin
from rest_framework import routers
from rest_framework_swagger.views import get_swagger_view

from server import settings
from smart_running import views

router = routers.DefaultRouter()
router.register(r'routes', views.RouteViewSet)
router.register(r'markers', views.MarkerViewSet)

api_docs = get_swagger_view(title='Smart Running API')

urlpatterns = [
    url(r'^', include(router.urls)),
    url(r'^api/', api_docs),
    url(r'^admin/', admin.site.urls),
    url(r'^auth/', include('rest_auth.urls')),
    url(r'^auth/registration/', include('rest_auth.registration.urls')),

    url(r'^site_media/(?P<path>.*)$', 'django.views.static.serve',
        {'document_root': settings.STATIC_ROOT, 'show_indexes': False}),
    url(r'^static/(?P<path>.*)$', 'django.views.static.serve',
        {'document_root': settings.STATIC_ROOT, 'show_indexes': False}),
]
