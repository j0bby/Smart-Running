from django.conf.urls import include, url
from django.contrib import admin
from rest_framework import routers

from smart_running import views
from rest_framework_swagger.views import get_swagger_view

router = routers.DefaultRouter()
router.register(r'routes', views.RouteViewSet)
router.register(r'markers', views.MarkerViewSet)
router.register(r'users', views.UserViewSet)

api_docs = get_swagger_view(title='Smart Running API')

urlpatterns = [
    url(r'^', include(router.urls)),
    url(r'^api/', api_docs),
    url(r'^admin/', admin.site.urls),
    url(r'^auth/', include('rest_framework.urls', namespace='rest_framework')),
]
