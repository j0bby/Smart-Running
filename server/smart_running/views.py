from django import forms
from django.core.exceptions import ValidationError
from rest_framework import viewsets, permissions, status
from rest_framework.decorators import detail_route
from rest_framework.permissions import BasePermission, IsAuthenticated, IsAuthenticatedOrReadOnly
from rest_framework.response import Response

from smart_running.models import Route, Marker, RouteRating, CompletedRoute
from smart_running.serializers import RouteSerializer, MarkerSerializer, CompletedRouteSerializer


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


class RouteRatingForm(forms.Form):
    rating = forms.IntegerField(min_value=1, max_value=5, required=True)


class RouteViewSet(viewsets.ModelViewSet):
    queryset = Route.objects.all()
    serializer_class = RouteSerializer
    permission_classes = (AdminWriteAnonRead,)

    def create(self, request, *args, **kwargs):
        request.data['publisher_id'] = request.auth.user.id
        return super(viewsets.ModelViewSet, self).create(request, *args, **kwargs)

    @detail_route(methods=['get', 'post'], permission_classes=[IsAuthenticated])
    def rate(self, request, pk, *args, **kwargs):
        if request.method == 'GET':
            try:
                rate_obj = RouteRating.objects.get(user=request.user, route=pk)
                rate = rate_obj.rating

            except RouteRating.DoesNotExist:
                rate = 0
            return Response({"rating": rate})

        elif request.method == 'POST':
            form = RouteRatingForm(request.POST or request.data)
            if form.is_valid():
                route = Route.objects.get(pk=pk)
                rating_value = form.cleaned_data['rating']
                rating, created = RouteRating.objects.get_or_create(user=request.user, route=route,
                                                                    defaults={
                                                                        'route': route,
                                                                        'user': request.user,
                                                                        'rating': rating_value
                                                                    })
                if not created:
                    rating.rating = rating_value
                rating.save()

                return Response(status=status.HTTP_204_NO_CONTENT)

            err = next(iter(form.errors.items()))
            return Response(err, status=status.HTTP_400_BAD_REQUEST)


class MarkerViewSet(viewsets.ModelViewSet):
    queryset = Marker.objects.all()
    serializer_class = MarkerSerializer
    permission_classes = (AdminWriteAnonRead,)


class CompletedRouteViewSet(viewsets.ModelViewSet):
    queryset = CompletedRoute.objects.all()
    serializer_class = CompletedRouteSerializer
    permission_classes = (IsAuthenticatedOrReadOnly,)

    def create(self, request, *args, **kwargs):
        self_id = request.user.id
        request.data['user'] = str(self_id)
        return viewsets.ModelViewSet.create(self, request, *args, **kwargs)

    def retrieve(self, request, pk=None, *args, **kwargs):
        try:
            queryset = self.filter_queryset(self.get_queryset()).filter(user=pk)
        except ValueError:
            queryset = self.get_queryset().none()

        page = self.paginate_queryset(queryset)
        if page is not None:
            serializer = self.get_serializer(page, many=True)
            return self.get_paginated_response(serializer.data)

        serializer = self.get_serializer(queryset, many=True)
        return Response(serializer.data)
