from django.conf.urls import include, url
from django.contrib import admin

# router = routers.DefaultRouter()
# router.register(r'routes', views.UserViewSet)
# router.register(r'markers', views.GroupViewSet)
from rest_framework_swagger.views import get_swagger_view

api_docs = get_swagger_view(title='Smart Running API')

urlpatterns = [
    # url(r'^', include(router.urls)),
    url(r'^$', api_docs),
    url(r'^admin/', admin.site.urls),
    url(r'^auth/', include('rest_framework.urls', namespace='rest_framework')),
]
