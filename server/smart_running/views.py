from rest_framework import viewsets, permissions
from rest_framework.permissions import BasePermission

from smart_running.models import Route, Marker
from smart_running.serializers import RouteSerializer, MarkerSerializer


class AdminWriteAnonRead(BasePermission):
    """
    If the request is a write request, the user must be an admin.
    Otherwise if it's a read request, anyone is allowed.
    """

    def has_permission(self, request, view):
        if request.method in permissions.SAFE_METHODS:
            return True

        return request.user and \
               permissions.is_authenticated(request.user) and \
               request.user.is_staff


class RouteViewSet(viewsets.ModelViewSet):
    queryset = Route.objects.all()
    serializer_class = RouteSerializer
    permission_classes = (AdminWriteAnonRead,)


class MarkerViewSet(viewsets.ModelViewSet):
    queryset = Marker.objects.all()
    serializer_class = MarkerSerializer
    permission_classes = (AdminWriteAnonRead,)
